package pti.test.server.beans;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.model.UploadedFile;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.Type;

import java.io.IOException;

import static org.junit.Assert.*;

public class ProductAddBeanTest {

    private static String name = "name";
    private static double price = 10d;
    private static int count = 10;
    private static UploadedFile uploadedFile = new DefaultUploadedFile();
    private static String country = "country";
    private static String supplier = "supplier";

    @BeforeClass
    public static void init() {
        name = "name";
        price = 10d;
        count = 10;
        uploadedFile = new DefaultUploadedFile();
        country = "country";
        supplier = "supplier";
    }


    @Test
    public void createProduct() throws IOException {
        //assertNotNull(new ProductAddBean().createProduct());
    }

    @Test
    public void typesInit() {
    }

    @Test
    public void upload() {
    }

    @Test
    public void createProduct1() {
    }

    @Test
    public void getTypes() {
    }

    @Test
    public void upload1() {
    }
}