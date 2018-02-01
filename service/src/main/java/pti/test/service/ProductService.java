package pti.test.service;

import pti.test.model.Product;
import pti.test.model.Type;

import java.util.List;

public interface ProductService {

    void delete(Product p);

    Product save(Product p);

    List<Product> findAll();

    Product findByIpk(long ipk);

    Product findByName(String name);

    void updateCount(int value, long ipk);

    boolean existsByIpk(long ipk);

    long count();

    boolean existsByName(String name);

}
