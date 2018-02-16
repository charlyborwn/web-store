package pti.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pti.test.dao.ProductCRUD;
import pti.test.dao.utils.ProductUtils;
import pti.test.model.Product;

import java.util.List;

/**
 * This class is responsible for requests delegation from controller level to DAO level.
 * @author Syrotyuk R.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductCRUD productCRUD;

    @Autowired
    private ProductUtils productUtils;

    @Override
    public void delete(Product p) {
        productCRUD.delete(p);
    }

    @Override
    public Product save(Product p) {
        return productCRUD.save(p);
    }

    @Override
    public List<Product> findAll() {
        return productCRUD.findAll();
    }

    @Override
    public Product findByIpk(long ipk) {
        return productCRUD.findByIpk(ipk);
    }

    @Override
    public Product findByName(String name) {

        return productCRUD.findFirstByName(name);
    }

    @Override
    public boolean existsByIpk(long ipk) {
        return productCRUD.existsByIpk(ipk);
    }

    @Override
    public void updateCount(int value, long ipk) {
        productUtils.updateCount(value, ipk);
    }

    @Override
    public long count() {
        return productCRUD.count();
    }

    @Override
    public boolean existsByName(String name){
        return productCRUD.existsByName(name);
    }

}
