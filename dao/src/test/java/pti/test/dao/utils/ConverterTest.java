package pti.test.dao.utils;

import org.junit.BeforeClass;
import org.junit.Test;
import pti.test.model.Comment;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.Product;
import pti.test.model.Type;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ConverterTest {

    private static ProductDTO productDTO;
    private static Product product;

    @BeforeClass
    public static void init() {
        productDTO = new ProductDTO();
        productDTO.setId(1);
        productDTO.setName("Name");
        productDTO.setCount(1);
        productDTO.setComments(new Comment(new HashMap<>()));
        productDTO.setCountry("Country");
        productDTO.setInfo(new HashMap<>());
        productDTO.setIpk(1);
        productDTO.setPic("pic");
        productDTO.setPics(new ArrayList<>());
        productDTO.setPrice(1d);
        productDTO.setSupplier("Supplier");
        productDTO.setType(new Type());

        product = new Product();
        product.setId(2);
        product.setName("Name1");
        product.setCount(2);
        product.setComments(new Comment(new HashMap<>()));
        product.setCountry("Country1");
        product.setInfo(new HashMap<>());
        product.setIpk(2);
        product.setPic("pic1");
        product.setPics(new ArrayList<>());
        product.setPrice(2d);
        product.setSupplier("Supplier1");
        product.setType(new Type());
    }

    @Test
    public void fromDTO() {
        Product product = Converter.fromDTO(productDTO);
        assertTrue(
                productDTO.getId() == product.getId() &&
                        productDTO.getName().equals(product.getName()) &&
                        productDTO.getCount() == product.getCount() &&
                        productDTO.getComments() == product.getComments() &&
                        productDTO.getCountry().equals(product.getCountry()) &&
                        productDTO.getInfo() == product.getInfo() &&
                        productDTO.getIpk() == product.getIpk() &&
                        productDTO.getSupplier().equals(product.getSupplier()) &&
                        productDTO.getPic().equals(product.getPic()) &&
                        productDTO.getPics() == product.getPics() &&
                        productDTO.getPrice() == product.getPrice() &&
                        productDTO.getType() == product.getType()
        );
    }

    @Test
    public void toDTO() {
        ProductDTO productDTO = Converter.toDTO(product);
        assertTrue(
                productDTO.getId() == product.getId() &&
                        productDTO.getName().equals(product.getName()) &&
                        productDTO.getCount() == product.getCount() &&
                        productDTO.getComments() == product.getComments() &&
                        productDTO.getCountry().equals(product.getCountry()) &&
                        productDTO.getInfo() == product.getInfo() &&
                        productDTO.getIpk() == product.getIpk() &&
                        productDTO.getSupplier().equals(product.getSupplier()) &&
                        productDTO.getPic().equals(product.getPic()) &&
                        productDTO.getPics() == product.getPics() &&
                        productDTO.getPrice() == product.getPrice() &&
                        productDTO.getType() == product.getType()
        );
    }

}