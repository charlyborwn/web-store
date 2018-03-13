package dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pti.test.dao.ProductCRUD;
import pti.test.dao.TypeCRUD;
import pti.test.model.Product;
import pti.test.model.Type;
import pti.test.server.config.PersistenceJPAConfig;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Syrotyuk R.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PersistenceJPAConfig.class)
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductCRUDTest {

    @Autowired
    private ProductCRUD productCRUD;

    @Autowired
    private TypeCRUD typeCRUD;

    private int typesCount = 50;
    private int productsCount = 50;

    @Test
    public void addProduct() {
        long before = productCRUD.count();
        Utils.createRandomTypes(typesCount).forEach(x -> typeCRUD.save(x));
        List<Product> products = Utils.createRandomProducts(productsCount, typeCRUD.findAll());
        products.forEach(x -> productCRUD.save(x));
        assertThat(productCRUD.findAll().size() - before == productsCount).isTrue();
    }

    @Test
    public void updateProduct() {
        List<Type> types = Utils.createRandomTypes(2);
        types.forEach(x -> typeCRUD.save(x));
        Product product = Utils.createRandomProducts(1, typeCRUD.findAll()).get(0);
        product.setType(types.get(0));
        productCRUD.save(product);
        product.setType(types.get(1));
        productCRUD.save(product);
        assertThat(productCRUD.findOne(product.getId()).getType().equals(types.get(1))).isTrue();
    }

    @Test
    public void deleteProduct() {
        Utils.createRandomTypes(typesCount).forEach(x -> typeCRUD.save(x));
        List<Product> products = Utils.createRandomProducts(productsCount, typeCRUD.findAll());
        products.forEach(x -> productCRUD.save(x));
        products.forEach(x -> productCRUD.delete(x));
        assertThat(Collections.disjoint(productCRUD.findAll(), products)).isTrue();
    }

}
