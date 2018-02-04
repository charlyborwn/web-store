package pti.test.server.beans;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pti.test.model.DTO.ProductDTO;
import pti.test.server.MyProperties;
import pti.test.server.interfaces.ImagesEngine;
import pti.test.server.interfaces.ProductEngine;

import javax.faces.bean.ManagedBean;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

@ManagedBean
@RestController
public class ImagesEngineImpl implements ImagesEngine {

    private static final int NUM_THREADS = 8;

    @Autowired
    private ProductEngine productEngine;

    @Autowired
    private Logger logger;

    @Override
    public String writeImage(BufferedImage image, String format, String address, String name) {
        String path = MyProperties.getInstance().getProperty("images_path").concat("/toys/");
        File file = new File(path + address);
        if (!file.exists()) {
            file.mkdirs();
        }
        String imageAddress = null;
        try {
            ImageIO.write(image, format, new File(file.getAbsolutePath().concat("/").concat(name)));
            imageAddress = "/toys/" + address.concat(name);
        } catch (IOException io) {
            logger.error(io.getMessage());
        }
        return imageAddress;

    }

    @Override
    public BufferedImage resizeImage(UploadedFile upl, int width, int height) throws IOException {
        BufferedImage input = ImageIO.read(upl.getInputstream());
        if (width == -1 | height == -1) {
            width = input.getWidth();
            height = input.getHeight();
            if (width > height) {
                height = (int) (600 * ((double) height / (double) width));
                width = 600;
            } else {
                width = (int) (400 * ((double) width / (double) height));
                height = 400;
            }
        }
        int imageType = input.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : input.getType();
        BufferedImage output = new BufferedImage(width, height, imageType);
        Graphics2D g2d = output.createGraphics();
        g2d.drawImage(input, 0, 0, width, height, null);
        g2d.dispose();
        logger.info("Image " + upl.getFileName() + " has been resized.");
        return output;
    }

    @Override
    public StreamedContent streamedImage(String address) {
        String path = MyProperties.getInstance().getProperty("images_path");
        File file = new File(path + address);
        DefaultStreamedContent streamedContent = new DefaultStreamedContent();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] b = new byte[bis.available()];
            bis.read(b);
            streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(b));
        } catch (IOException io) {
            logger.error(io.getMessage());
        }
        return streamedContent;
    }

    @Override
    public boolean clearImagesFromFileSystem(long ipk) {
        String path = MyProperties.getInstance().getProperty("images_path").concat("/toys/");
        File file = new File(path.concat(String.valueOf(ipk)).concat("/images/"));
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            Arrays.asList(Objects.requireNonNull(files)).forEach(File::delete);
        } else {
            logger.warn(path.concat(String.valueOf(ipk)).concat("/images/") + " not exist.");
        }
        return true;
    }

    @Override
    public boolean deleteProductImages(long ipk) {
        String path = MyProperties.getInstance().getProperty("images_path").concat("/toys/");
        File file = new File(path.concat(String.valueOf(ipk)).concat("/images/"));
        boolean deleteImages = clearImagesFromFileSystem(ipk);
        boolean deleteFolder = false;
        boolean deleteMainImage = false;
        if (file.exists()) {
            deleteFolder = file.delete();
            logger.info("Path " + path.concat(String.valueOf(ipk)) + " was deleted.");
        } else {
            logger.error("Path " + path.concat(String.valueOf(ipk)).concat("/images/") + " does not exist.");
        }
        File productDirectory = new File(path.concat(String.valueOf(ipk)));
        if (productDirectory.exists()) {
            Arrays.asList(Objects.requireNonNull(productDirectory.listFiles())).forEach(File::delete);
            deleteMainImage = true;
            deleteFolder = productDirectory.delete();
        } else {
            logger.error("Path " + path.concat(String.valueOf(ipk)) + " does not exist.");
        }
        logger.info("Product files was deleted: ID " + ipk);
        return deleteImages & deleteFolder & deleteMainImage;
    }

    @Override
    public void serveFileSystem(long ipk) {
        String path = MyProperties.getInstance().getProperty("images_path");
        File mainImagePath = new File(path.concat("/toys/").concat(String.valueOf(ipk)));
        String imageAddress = path.concat(productEngine.findByIpk(ipk).getPic()).replaceAll("//", "/");
        if (mainImagePath.exists()) {
            for (File f : Objects.requireNonNull(mainImagePath.listFiles())) {
                if (f.isFile() && !f.getAbsolutePath().replaceAll("\\\\", "/").equals(imageAddress)) {
                    f.delete();
                }
            }
        }
        File imagesPath = new File(path.concat("/toys/").concat(String.valueOf(ipk)).concat("/images/"));
        ProductDTO product = productEngine.findByIpk(ipk);
        ArrayList<String> productImages = product.getPics();
        ArrayList<String> images = productImages.stream()
                .map(x -> path.concat(x).replaceAll("//", "/"))
                .collect(Collectors.toCollection(ArrayList::new));
        if (!imagesPath.exists() || imagesPath.listFiles().length == 0) {
            logger.warn("No images in product folder: ID " + ipk);
            productImages.clear();
            return;
        }
        List<String> imageAddresses = Arrays.stream(Objects.requireNonNull(imagesPath.listFiles()))
                .map(File::getAbsolutePath).collect(Collectors.toList());
        for (String s : images) {
            File image = new File(s);
            if (!image.exists()) {
                productImages.remove(s.replace(path, ""));
                logger.info("Not valid image link was deleted. Link: " + s.replace(path, "") + ", ID: " + ipk);
            }
        }
        for (String s : imageAddresses) {
            if (!images.contains(s.replaceAll("\\\\", "/"))) {
                File image = new File(s);
                image.delete();
                logger.info("Image is not in DB and was deleted. Link: " + s + ", ID: " + ipk);
            }
        }
        product.setPics(productImages);
        productEngine.save(product);
        logger.info("Product folder processing ended.");
    }

    @Override
    public void deleteImage(String address) {
        String path = MyProperties.getInstance().getProperty("images_path");
        File image = new File(path.concat(address));
        if (image.exists()) {
            image.delete();
            logger.info("Image " + image.getName() + " was deleted.");
        }
    }

    @Override
    public void reviewFS() {
        List<Long> products = productEngine.getProducts().stream().map(ProductDTO::getIpk).collect(Collectors.toList());
        String path = MyProperties.getInstance().getProperty("images_path").concat("/toys/");
        List<String> folders = Arrays.asList(new File(path).listFiles()).stream().map(x -> x.getName()).collect(Collectors.toList());
        for (String f : folders) {
            try {
                if (!products.contains(Long.valueOf(f))) {
                    deleteFolder(new File(path.concat("/") + f));
                }
            } catch (NumberFormatException e) {
                deleteFolder(new File(path.concat("/") + f));
            }
        }
        ForkJoinPool pool = new ForkJoinPool(8);
        Integer invoke = pool.invoke(new FileReviewer(0, products.size(), products.size(), products));
        System.out.println(invoke);
    }

    private synchronized void deleteFolder(File folder) {
        if (!folder.exists()) {
            return;
        }
        List<File> files = Arrays.asList(folder.listFiles());
        if (folder.getAbsolutePath().replaceAll("\\\\", "/")
                .equals(MyProperties.getInstance().getProperty("images_path").concat("/toys"))) {
            return;
        }
        if (files.size() > 10) {
            return;
        }
        if (files.size() == 0) {
            folder.delete();
            deleteFolder(folder.getParentFile());
        }
        for (File f : files) {
            if (f.isFile()) {
                f.delete();
            }
        }
        if (folder.exists()) {
            List<File> files1 = Arrays.asList(folder.listFiles());
            for (File f : files1) {
                deleteFolder(f);
            }
            deleteFolder(folder);
        }
        return;

    }

    class FileReviewer extends RecursiveTask<Integer> {
        private int from, to;
        private int N;
        private List<Long> products;

        public FileReviewer(int from, int to, int n, List<Long> products) {
            this.from = from;
            this.to = to;
            N = n;
            this.products = products;
        }

        @Override
        protected Integer compute() {
            if ((to - from) <= N / NUM_THREADS) {
                for (int i = from; i < to; i++) {
                    long ipk = products.get(i);
                    serveFileSystem(ipk);
                }
                return to - from;
            } else {
                int mid = (from + to) / 2;
                FileReviewer half1 = new FileReviewer(from, mid, N, products);
                half1.fork();
                FileReviewer half2 = new FileReviewer(mid, to, N, products);
                Integer result = half2.compute();
                return half1.join() + result;
            }
        }
    }


}
