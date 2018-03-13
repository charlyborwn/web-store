package pti.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pti.test.dao.ProductCRUD;
import pti.test.dao.utils.ProductUtils;
import pti.test.model.Product;

import java.util.List;

/**
 * This class is responsible for requests delegation from controller
 * level to DAO level.
 *
 * @author Syrotyuk R.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductCRUD productCRUD;

    @Autowired
    private ProductUtils productUtils;

    /**
     * Gets all entities from product table in DB and puts its to the cache
     * and then reads from it.
     *
     * @return all entities from users table in DB
     */
    @Override
    public List<Product> findAll() {
        return productCRUD.findAll();
    }

    /**
     * Finds entity with certain <code>ipk</code> external key.
     *
     * @param ipk external key
     * @return entity with certain <code>ipk</code> external key
     */
    @Override
    public Product findByIpk(long ipk) {
        return productCRUD.findByIpk(ipk);
    }

    /**
     * Finds entity with certain <code>name</code> product name.
     *
     * @param name product name
     * @return entity with certain <code>name</code> product name
     */
    @Override
    public Product findByName(String name) {

        return productCRUD.findFirstByName(name);
    }

    /**
     * Saves the entity to Products table both in the cache and DB or updates the existing entity.
     *
     * @param p <code>Product</code> to be saved or updated
     * @return saved or updated entity
     */
    @Override
    public Product save(Product p) {
        return productCRUD.save(p);
    }

    /**
     * Updates directly a count <code>Product</code> field value of the <code>Product</code>.
     * Uses the cache for fast working.
     *
     * @param value a new value
     * @param ipk   individual product key
     */
    @Override
    public void updateCount(int value, long ipk) {
        productUtils.updateCount(value, ipk);
    }

    /**
     * Deletes the entity from Products table both in the cache and DB.
     *
     * @param p <code>Product</code> to be deleted
     */
    @Override
    public void delete(Product p) {
        productCRUD.delete(p);
    }

    /**
     * Deletes the entity with given individual primary key from
     * products table both in the cache and DB.
     *
     * @param ipk product individual primary key
     */
    @Override
    public void deleteByIpk(long ipk) {
        productCRUD.deleteByIpk(ipk);
    }

    /**
     * Obtains the existence of product in database by its individual
     * primary key.
     *
     * @param ipk product individual primary key
     * @return true if product exists, false in other case
     */
    @Override
    public boolean existsByIpk(long ipk) {
        return productCRUD.existsByIpk(ipk);
    }

    /**
     * Obtains the count of products in table.
     *
     * @return the count of products in table
     */
    @Override
    public long count() {
        return productCRUD.count();
    }

    /**
     * Obtains the existence of product in database by its name.
     *
     * @param name product name
     * @return true if product exists, false in other case
     */
    @Override
    public boolean existsByName(String name) {
        return productCRUD.existsByName(name);
    }

}
