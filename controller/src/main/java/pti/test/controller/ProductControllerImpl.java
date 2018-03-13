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
 * <code>Product</code> controller.
 *
 * @author Syrotyuk R.
 */
@RestController
@RequestMapping(value = "/product")
public class ProductControllerImpl implements Controller {

    @Autowired
    private ProductService productService;

    /**
     * Obtains a request at defined URL and returns the list
     * of all products that exist in database.
     *
     * @return the list of all products that exist in database
     */
    @Override
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<ProductDTO> findAll() {
        return Converter.DTOList(productService.findAll());
    }

    /**
     * Obtains a request at defined URL and returns the product
     * with specified individual product key.
     *
     * @param ipk specified individual product key
     * @return the product with specified individual product key
     */
    @Override
    @RequestMapping(value = "/{ipk}", method = RequestMethod.GET)
    public ProductDTO findByIpk(@PathVariable(value = "ipk") long ipk) {
        return Converter.toDTO(productService.findByIpk(ipk));
    }

    /**
     * Obtains a request at defined URL and returns the product
     * with specified name.
     *
     * @param name specified product name
     * @return the product with specified name
     */
    @Override
    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET)
    public ProductDTO findByName(@PathVariable(value = "name") String name) {
        return Converter.toDTO(productService.findByName(name));
    }

    /**
     * Obtains a request at defined URL and updates or saves the entity
     * with specified individual primary key.
     *
     * @param product updated product entity
     * @return updated entity
     */
    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    public Product save(@RequestBody ProductDTO product) {
        return productService.save(Converter.fromDTO(product));
    }

    /**
     * Obtains a request at defined URL and updates the product
     * count.
     *
     * @param ipk   specified individual product key
     * @param count new product count
     */
    @Override
    @RequestMapping(value = "/update_count/{ipk}/{count}", method = RequestMethod.POST)
    public void updateCount(@PathVariable(value = "ipk") long ipk, @PathVariable(value = "count") int count) {
        productService.updateCount(count, ipk);
    }

    /**
     * Obtains a request at defined URL and deletes the product
     * with given name.
     *
     * @param product product object to be deleted
     */
    @Override
    @RequestMapping(value = "/delete/", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    public void delete(@RequestBody ProductDTO product) {
        productService.delete(Converter.fromDTO(product));
    }

    /**
     * Obtains a request at defined URL and deletes the product
     * with given individual primary key.
     *
     * @param ipk product individual primary key
     */
    @Override
    @RequestMapping(value = "/delete/{ipk}", method = RequestMethod.DELETE)
    public void deleteByIpk(@PathVariable(value = "ipk") long ipk) {
        productService.deleteByIpk(ipk);
    }

    /**
     * Obtains a request at defined URL and returns true if
     * product with specified name exists and false in other
     * case.
     *
     * @param name specified product name
     * @return true if product with specified name exists
     * and false in other case
     */
    @Override
    @RequestMapping(value = "/exists/name/{name}", method = RequestMethod.GET)
    public boolean existsByName(@PathVariable(value = "name") String name) {
        return productService.existsByName(name);
    }

    /**
     * Obtains a request at defined URL and returns true if
     * product with specified individual primary key exists
     * and false in other case.
     *
     * @param ipk individual primary key
     * @return true if product with specified individual primary
     * key exists and false in other case
     */
    @Override
    @RequestMapping(value = "/exists/{ipk}", method = RequestMethod.GET)
    public boolean existsByIpk(@PathVariable(value = "ipk") long ipk) {
        return productService.existsByIpk(ipk);
    }

    /**
     * Obtains a request at defined URL and obtains the unique
     * products kinds count.
     *
     * @return the count of existing products
     */
    @Override
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public long count() {
        return productService.count();
    }

}
