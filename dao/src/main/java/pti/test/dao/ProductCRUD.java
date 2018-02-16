package pti.test.dao;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pti.test.model.Product;

import java.util.List;

/**
 * This interface contains a methods for working with DB and
 * extends <code>CrudRepository</code> that allow to be confined
 * to only methods declaration.
 * @author Syrotyuk R.
 */
@Repository
public interface ProductCRUD extends CrudRepository<Product, Long> {

    /**
     * Deletes the entity from Products table both in the cache and DB.
     *
     * @param p <code>Product</code> to be deleted
     */

    @CacheEvict(value = {"product1", "product2", "product3", "product4", "product5"}, key = "#p0.id", allEntries = true)
    void delete(Product p);

    /**
     * Saves the entity to Products table both in the cache and DB or updates the existing entity.
     *
     * @param p <code>Product</code> to be saved or updated
     * @return saved or updated entity
     */
    @CacheEvict(value = {"product1", "product2", "product3", "product4", "product5"}, key = "#p0.id", allEntries = true)
    Product save(Product p);

    /**
     * Gets all entities from product table in DB and puts its to the cache
     * and then reads from it.
     *
     * @return all entities from users table in DB
     */

    @Cacheable({"product1", "product2"})
    List<Product> findAll();

    /**
     * Finds entity with certain <code>ipk</code> external key.
     *
     * @param ipk external key
     * @return entity with certain <code>ipk</code> external key
     */
    @Cacheable({"product3"})
    Product findByIpk(long ipk);

    /**
     * Finds entity with certain <code>name</code> product name.
     *
     * @param name product name
     * @return entity with certain <code>name</code> product name
     */
    @Cacheable({"product4"})
    Product findFirstByName(String name);

    /**
     * Obtains the count of products in table.
     *
     * @return the count of products in table
     */
    long count();

    @Cacheable({"product5"})
    boolean existsByIpk(long ipk);

    @Cacheable({"product5"})
    boolean existsByName(String name);

}
