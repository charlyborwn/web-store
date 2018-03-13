package pti.test.server.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pti.test.controller.Controller;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.Product;
import pti.test.server.interfaces.ProductEngine;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.*;

/**
 * This class is responsible for all products operation delegation
 * from Spring managed beans to controller level.
 *
 * @author Syrotyuk R.
 */
@ManagedBean
@RestController
@ViewScoped
public class ProductEngineImpl implements ProductEngine {

    @Autowired
    private Controller controller;

    /**
     * Obtains the list of all products.
     *
     * @return the list of all products
     */
    @Override
    public List<ProductDTO> getProducts() {
        return controller.findAll();
    }

    /**
     * Finds the product by it name.
     *
     * @param name product name
     * @return product with specified name or null if such product not exists.
     */
    @Override
    public ProductDTO findByName(String name) {
        if (controller.existsByName(name)) {
            return controller.findByName(name);
        } else {
            return null;
        }
    }

    /**
     * Finds the product with specified individual product key.
     *
     * @param ipk specified individual product key
     * @return the product with specified individual product key
     */
    @Override
    public ProductDTO findByIpk(long ipk) {
        if (controller.existsByIpk(ipk)) {
            return controller.findByIpk(ipk);
        } else {
            return null;
        }
    }

    /**
     * Saves or updates the product entity.
     *
     * @param product product entity to be saved or updated
     * @return saved or updated product entity
     */
    @Override
    public Product save(ProductDTO product) {
        return controller.save(product);
    }

    /**
     * Updates the product count.
     *
     * @param value new count value
     * @param ipk   individual primary key
     */
    @Override
    public void updateCount(int value, long ipk) {
        controller.updateCount(ipk, value);
    }

    /**
     * Deletes the product with given name.
     *
     * @param product product object to be deleted
     */
    @Override
    public void delete(ProductDTO product) {
        controller.delete(product);
    }

    /**
     * Obtains the unique products kinds count.
     *
     * @return the count of existing products
     */
    @Override
    public long count() {
        return controller.count();
    }

}
