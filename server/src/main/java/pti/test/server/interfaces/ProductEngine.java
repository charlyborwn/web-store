package pti.test.server.interfaces;

import com.sun.scenario.effect.impl.prism.PrDrawable;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.Product;

import java.util.List;

public interface ProductEngine {

    List<ProductDTO> getProducts();

    Product save(ProductDTO product);

    void updateCount(int value, long ipk);

    ProductDTO findByName(String name);

    ProductDTO findByIpk(long ipk);

    void delete(ProductDTO product);

    long count();

}
