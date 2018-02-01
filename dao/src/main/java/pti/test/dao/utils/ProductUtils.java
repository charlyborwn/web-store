package pti.test.dao.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.Product;
import pti.test.model.Type;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for direct commands invocation
 * without any instance creations.
 */
@Repository
@Transactional
public class ProductUtils {

    @Autowired
    EntityManager entityManager;

//    /**
//     * Obtains a field value of certain <code>Product</code>. Uses the cache for fast working.
//     * @param column name of table column
//     * @param ipk external product key
//     * @return a collection of certain field values
//     */
//    public List<String> getOnlyColumn(String column, long ipk) {
//        List<String> result = new ArrayList<>();
//        try {
//            String hql = "SELECT p." + column + " FROM product p where p.ipk = :ipk";
//            Query query = entityManager.createQuery(hql);
//            query.setParameter("ipk", ipk);
//            result = query.getResultList();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return result;
//    }

    /**
     * Updates directly a count <code>Product</code> field value of the <code>Product</code>.  Uses the cache for fast working.
     * @param value a new value
     * @param ipk external product key
     */
    @CacheEvict(value = {"product1", "product2", "product3", "product4"}, key = "#p0.id", allEntries = true)
    public void updateCount(int value, long ipk) {
        try {
            String hql = "UPDATE product p SET p.count = " + value + " where p.ipk = :ipk";
            Query query = entityManager.createQuery(hql);
            query.setParameter("ipk", ipk);
            query.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

//    /**
//     * Obtains a collection of certain <code>Product</code> field values.
//     * @param column name of table column
//     * @returna collection of certain field values
//     */
//    public List<Object> getOnlyColumn(String column) {
//        List<Object> result = new ArrayList<>();
//        try {
//            String hql = "SELECT p." + column + " FROM product p";
//            Query query = entityManager.createQuery(hql);
//            result = query.getResultList();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return result;
//    }

}
