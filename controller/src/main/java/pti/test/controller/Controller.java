package pti.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pti.test.dao.utils.Converter;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.Product;
import pti.test.model.Type;
import pti.test.service.ProductService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * <code>PRODUCT</code> controller.
 */
public interface Controller {

    Product save(ProductDTO product);

    void delete(ProductDTO product);

    ProductDTO findByName(String name);

    List<ProductDTO> findAll();

    ProductDTO findByIpk(long ipk);

    boolean existsByName(String name);

    void updateCount(int value, long ipk);

    boolean existsByIpk(long ipk);

    long count();

}
