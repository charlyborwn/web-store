package pti.test.controller;

import pti.test.model.DTO.ProductDTO;
import pti.test.model.Product;

import java.util.List;

/**
 * The interface for <code>Product</code> controller.
 *
 * @author Syrotyuk R.
 */
public interface Controller {

    /**
     * Returns the list of all products that exist in database.
     *
     * @return the list of all products that exist in database
     */
    List<ProductDTO> findAll();

    /**
     * Returns the product with specified name.
     *
     * @param name specified product name
     * @return the product with specified name
     */
    ProductDTO findByName(String name);

    /**
     * Returns the product with specified individual product key.
     *
     * @param ipk specified individual product key
     * @return the product with specified individual product key
     */
    ProductDTO findByIpk(long ipk);

    /**
     * Updates or saves the entity with specified individual primary key.
     *
     * @param product updated product entity
     * @return updated entity
     */
    Product save(ProductDTO product);

    /**
     * Updates the product count.
     *
     * @param ipk   specified individual product key
     * @param count new product count
     */
    void updateCount(long ipk, int count);

    /**
     * Deletes the product with given name.
     *
     * @param product product object to be deleted
     */
    void delete(ProductDTO product);

    /**
     * Deletes the product with given individual primary key.
     *
     * @param ipk product individual primary key
     */
    void deleteByIpk(long ipk);

    /**
     * Returns true if product with specified name exists and false
     * in other case.
     *
     * @param name specified product name
     * @return true if product with specified name exists
     * and false in other case
     */
    boolean existsByName(String name);

    /**
     * Returns true if product with specified individual primary key
     * exists and false in other case.
     *
     * @param ipk individual primary key
     * @return true if product with specified individual primary
     * key exists and false in other case
     */
    boolean existsByIpk(long ipk);

    /**
     * Obtains the unique products kinds count.
     *
     * @return the count of existing products
     */
    long count();

}
