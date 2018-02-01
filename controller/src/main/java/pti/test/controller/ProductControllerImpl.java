package pti.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pti.test.dao.utils.Converter;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.Product;
import pti.test.service.ProductService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * <code>PRODUCT</code> controller.
 */
@RestController
@RequestMapping(value = "/shop/")
public class ProductControllerImpl implements Controller {

    @Autowired
    private ProductService productService;

    @Override
    @RequestMapping(value = "update/{index}", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    public Product save(@RequestBody ProductDTO product) {
        return productService.save(Converter.fromDTO(product));
    }

    @Override
    @RequestMapping(value = "products/delete/{product_name}", method = RequestMethod.POST)
    public void delete(@RequestBody ProductDTO product) {
        productService.delete(Converter.fromDTO(product));
    }

    @Override
    @RequestMapping(value = "products/{product_name}", method = RequestMethod.GET)
    public ProductDTO findByName(@PathVariable(value = "product_name") String product_name) {
        return Converter.toDTO(productService.findByName(product_name));
    }

    @Override
    @RequestMapping(value = "products", method = RequestMethod.GET)
    public List<ProductDTO> findAll() {
        return Converter.DTOList(productService.findAll());
    }

    @Override
    @RequestMapping(value = "find/{ipk}", method = RequestMethod.GET, consumes = APPLICATION_JSON_VALUE)
    public ProductDTO findByIpk(@PathVariable(value = "ipk") long ipk) {
        return Converter.toDTO(productService.findByIpk(ipk));
    }

    @Override
    public boolean existsByName(String name){
        return productService.existsByName(name);
    }

    @Override
    public void updateCount(int value, long ipk) {
        productService.updateCount(value, ipk);
    }

    @Override
    public boolean existsByIpk(long ipk) {
        return productService.existsByIpk(ipk);
    }

    @Override
    public long count() {
        return productService.count();
    }

}
