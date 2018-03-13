package pti.test.server.beans;


import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import pti.test.model.Comment;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.Type;
import pti.test.server.JSFMessages;
import pti.test.server.interfaces.ImagesEngine;
import pti.test.server.interfaces.ProductAdd;
import pti.test.server.interfaces.ProductEngine;
import pti.test.server.interfaces.TypeEngine;

import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class corresponds for adding new ProductDTO to DB in case of success verifying process.
 *
 * @author Syrotyuk R.
 */
@ManagedBean
@RestController
@Scope("view")
public class ProductAddBean implements ProductAdd {

    @Autowired
    private ProductEngine productEngine;

    @Autowired
    private ProductInfoBean infoBean;

    @Autowired
    private TypeEngine typeEngine;

    @Autowired
    private ImagesEngine imagesEngine;

    @Autowired
    private Logger logger;

    private String name;
    private String type;
    private String category;
    private double price;
    private int count;
    private UploadedFile uploadedFile;
    private String country;
    private String supplier;
    private ArrayList<BufferedImage> bufferedImages = new ArrayList<>();
    private Integer progress = 0;
    private int filesCount = 100000000;

    /**
     * Contains selected product category.
     */
    private String selection;

    /**
     * Calls <code>addProduct()</code> method to saveProduct <code>ProductDTO</code> product to DB if minimal conditions
     * are satisfied. Error message will be shown in case of existing a product with same name in DB.
     * If new product is completely filled by information recording in DB will occurs. If only
     * main information was entered recording in DB will occurs too but warning message will
     * be shown. In other case error message will be shown and no recording process will be happened.
     *
     * @throws IOException when any reading/writing error happens
     */
    @Override
    public void createProduct() throws IOException {

        if (name.equals("") | count == 0 | price == 0.0 | uploadedFile == null | country.equals("") | supplier.equals("")) {
            logger.error("Can not add the product. Try to fill all fields correctly.");
            JSFMessages.error("Can not add the product. Try to fill all fields correctly.");
        } else if (productEngine.findByName(name) != null) {
            logger.error("Product " + name + " already exists.");
            JSFMessages.error("Product " + name + " already exists.");
        } else if (typeEngine.getTypeByCategory(selection) == null) {
            logger.error("The product type is not specified.");
            JSFMessages.error("Specify the product type.");
        } else if (infoBean.getInfo().get("Date").equals("") | infoBean.getInfo().get("Description").equals("")) {
            addProduct();
            logger.warn("Operation success, but not all additional info added.");
            JSFMessages.warn("Operation success, but not all additional info added.");
        } else {
            addProduct();
            logger.info("New product successfully added.");
            JSFMessages.info("New product successfully added.");
        }

    }

    /**
     * Initiates all accessible product findAll and categories and writes its into accordant collections.
     */
    public List<SelectItem> getTypes() {
        List<SelectItem> types = new ArrayList<>();
        Set<String> typeNames = typeEngine.getTypeList().stream().map(Type::getType)
                .collect(Collectors.toCollection(TreeSet::new));
        for (String type : typeNames) {
            SelectItemGroup group = new SelectItemGroup(type);
            List<String> categories = typeEngine.getTypeList().stream().filter(x -> x.getType().equals(type))
                    .map(Type::getCategory).collect(Collectors.toList());
            SelectItem item[] = new SelectItem[categories.size()];
            for (int i = 0; i < categories.size(); i++) {
                item[i] = new SelectItem(categories.get(i), categories.get(i));
            }
            group.setSelectItems(item);
            types.add(group);
        }
        logger.info("Types was initiated.");
        return types;
    }

    /**
     * Generates random external key as mathematical module of random
     * <code>UUID</code> hash code <code>Long</code> representing.
     *
     * @return random external product ID
     */
    private long id() {
        return ((long) Math.abs(UUID.randomUUID().hashCode()));
    }

    /**
     * Creates <code>ProductDTO</code> object and record it into DB.
     *
     * @throws IOException when any reading/writing error happens
     */
    private void addProduct() throws IOException {
        ProductDTO newProduct = createNewProduct();
        saveProduct(newProduct);
    }

    /**
     * Creates <code>ProductDTO</code> instance
     * Main product image size is changed to 150x150 pixels.
     *
     * @return new <code>ProductDTO</code> instance
     * @throws IOException
     */
    private ProductDTO createNewProduct() throws IOException {
        ProductDTO p = new ProductDTO();
        p.setName(name);
        p.setCount(count);
        p.setPrice(price);
        p.setIpk(id());
        p.setType(typeEngine.getTypeByCategory(selection));
        p.setInfo(infoBean.getInfo());
        p.setPic(imagesEngine.writeImage(
                imagesEngine.resizeImage(uploadedFile, 150, 150), "png",
                String.valueOf(p.getIpk()).concat("/"), Math.abs(UUID.randomUUID().hashCode()) + ".png")
        );
        p.setPics(writeUploadedImages(p.getIpk()));
        p.setCountry(country);
        p.setSupplier(supplier);
        p.setComments(new Comment(new HashMap<>()));
        return p;
    }

    /**
     * Saves ProductDTO object to DB.
     *
     * @param p ProductDTO object
     */
    private void saveProduct(ProductDTO p) {
        productEngine.save(p);
    }

    /**
     * Uploads an image from file system with further resizing and writing to collection.
     *
     * @param event image upload event
     * @throws IOException when any reading/writing error happens
     */
    public synchronized void upload(FileUploadEvent event) throws IOException, InterruptedException {
        if (bufferedImages == null) {
            bufferedImages = new ArrayList<>();
        }
        bufferedImages.add(imagesEngine.resizeImage(event.getFile(), -1, -1));
        progress += 100 / filesCount;
        if (bufferedImages.size() == filesCount) {
            progress = 100;
        }
        logger.info("Upload Event: " + event.getFile().getFileName());
    }

    /**
     * Writes uploaded image to file system.
     *
     * @param ipk product external id, will be used as product folder name
     * @return
     */
    private ArrayList<String> writeUploadedImages(Long ipk) {
        ArrayList<String> images = new ArrayList<>();
        if (bufferedImages == null) {
            bufferedImages = new ArrayList<>();
        }
        bufferedImages.forEach(x -> {
            try {
                String localAddress = String.valueOf(ipk).concat("/images/");
                String address = imagesEngine.writeImage(x, "png", localAddress,
                        Math.abs(UUID.randomUUID().hashCode()) + ".png");
                images.add(address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        logger.info("Images addresses list created.");
        bufferedImages = null;
        return images;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public int getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
    }

    static int number = 2000;
    File file = new File("C:\\Users\\Romashka\\Desktop\\New folder");
    File[] files = file.listFiles();

    public void randomAdd() {
        for (int i = 0; i < 1000; i++) {
            ProductDTO p = new ProductDTO();
            p.setName("Random product " + ++number);
            p.setCount((int) (1000 * Math.random()));
            p.setIpk(id());
            NumberFormat nf = new DecimalFormat();
            nf.setMaximumFractionDigits(2);
            p.setPrice((int) (10000 * Math.random()));
            List<Type> typeList = typeEngine.getTypeList();
            int types = typeList.size();
            p.setType(typeList.get((int) (types * Math.random())));
            p.setSupplier("Roman");
            p.setCountry("Ukraine");
            HashMap<String, String> info = new HashMap<>();
            info.put("Warranty", String.valueOf((int) (10 * Math.random())));
            info.put("Description", "Info");
            info.put("Discount", String.valueOf((int) (50 * Math.random())));
            info.put("Date", "null");
            p.setInfo(info);
            int fileNum = (int) (files.length * Math.random());
            try {
                BufferedImage bufferedImage = ImageIO.read(files[fileNum]);
                int imageType = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
                BufferedImage output = new BufferedImage(150, 150, imageType);
                Graphics2D g2d = output.createGraphics();
                g2d.drawImage(bufferedImage, 0, 0, 150, 150, null);
                g2d.dispose();
                String image = imagesEngine.writeImage(output, "png",
                        String.valueOf(p.getIpk()).concat("/"), Math.abs(UUID.randomUUID().hashCode()) + ".png");
                p.setPic(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
            p.setPic("");
            p.setPics(new ArrayList<>());
            saveProduct(p);
        }
    }

}
