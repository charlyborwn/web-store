package pti.test.service;

import pti.test.model.Product;

import java.util.List;

/**
 * The interface for service class for products.
 *
 * @author Syrotyuk R.
 */
public interface ProductService {

    /**
     * Gets all entities from product table in DB and puts its to the cache
     * and then reads from it.
     *
     * @return all entities from users table in DB
     */
    List<Product> findAll();

    /**
     * Finds entity with certain <code>ipk</code> external key.
     *
     * @param ipk external key
     * @return entity with certain <code>ipk</code> external key
     */
    Product findByIpk(long ipk);

    /**
     * Finds entity with certain <code>name</code> product name.
     *
     * @param name product name
     * @return entity with certain <code>name</code> product name
     */
    Product findByName(String name);

    /**
     * Saves the entity to Products table both in the cache and DB or updates the existing entity.
     *
     * @param p <code>Product</code> to be saved or updated
     * @return saved or updated entity
     */
    Product save(Product p);

    /**
     * Updates directly a count <code>Product</code> field value of the <code>Product</code>.
     * Uses the cache for fast working.
     *
     * @param value a new value
     * @param ipk   individual product key
     */
    void updateCount(int value, long ipk);

    /**
     * Deletes the entity from Products table both in the cache and DB.
     *
     * @param p <code>Product</code> to be deleted
     */
    void delete(Product p);

    /**
     * Deletes the entity with given individual primary key from
     * products table both in the cache and DB.
     *
     * @param ipk product individual primary key
     */
    void deleteByIpk(long ipk);

    /**
     * Obtains the existence of product in database by its individual
     * primary key.
     *
     * @param ipk product individual primary key
     * @return true if product exists, false in other case
     */
    boolean existsByIpk(long ipk);

    /**
     * Obtains the count of products in table.
     *
     * @return the count of products in table
     */
    long count();

    /**
     * Obtains the existence of product in database by its name.
     *
     * @param name product name
     * @return true if product exists, false in other case
     */
    boolean existsByName(String name);

}
