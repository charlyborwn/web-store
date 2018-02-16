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
 * @author Syrotyuk R.
 */
@ManagedBean
@RestController
@ViewScoped
public class ProductEngineImpl implements ProductEngine {

    @Autowired
    private Controller controller;

    @Override
    public List<ProductDTO> getProducts() {
        return controller.findAll();
    }

    @Override
    public Product save(ProductDTO product) {
        return controller.save(product);
    }

    @Override
    public void updateCount(int value, long ipk) {
        controller.updateCount(value, ipk);
    }

    @Override
    public ProductDTO findByName(String name) {
        if (controller.existsByName(name)) {
            return controller.findByName(name);
        } else {
            return null;
        }
    }

    @Override
    public ProductDTO findByIpk(long ipk) {
        if (controller.existsByIpk(ipk)) {
            return controller.findByIpk(ipk);
        } else {
            return null;
        }
    }

    @Override
    public void delete(ProductDTO product) {
        controller.delete(product);
    }

    @Override
    public long count() {
        return controller.count();
    }

}
