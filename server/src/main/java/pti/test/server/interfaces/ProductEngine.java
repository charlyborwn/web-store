package pti.test.server.interfaces;

import pti.test.model.DTO.ProductDTO;
import pti.test.model.Product;

import java.util.List;

/**
 * @author Syrotyuk R.
 */
public interface ProductEngine {

    List<ProductDTO> getProducts();

    Product save(ProductDTO product);

    void updateCount(int value, long ipk);

    ProductDTO findByName(String name);

    ProductDTO findByIpk(long ipk);

    void delete(ProductDTO product);

    long count();

}
