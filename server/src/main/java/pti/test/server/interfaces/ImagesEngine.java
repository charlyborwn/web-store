package pti.test.server.interfaces;

import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImagesEngine {

    String writeImage(BufferedImage image, String format, String address, String name) throws IOException;

    BufferedImage resizeImage(UploadedFile upl, int width, int height) throws IOException;

    StreamedContent streamedImage(String address);

    boolean clearImagesFromFileSystem(long ipk);

    boolean deleteProductImages(long ipk);

    void serveFileSystem(long ipk);

    void deleteImage(String address);

}
