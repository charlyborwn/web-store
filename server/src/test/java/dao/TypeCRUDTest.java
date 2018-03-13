package dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pti.test.dao.TypeCRUD;
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
public class TypeCRUDTest {

    @Autowired
    private TypeCRUD typeCRUD;

    private int typesCount = 50;

    @Test
    public void addType() {
        long before = typeCRUD.count();
        List<Type> types = Utils.createRandomTypes(typesCount);
        types.forEach(x -> typeCRUD.save(x));
        assertThat(typeCRUD.findAll().size() - before == typesCount).isTrue();
    }

    @Test
    public void updateType() {
        Type type = typeCRUD.save(Utils.createRandomTypes(1).get(0));
        type.setCategory("NEW");
        typeCRUD.save(type);
        assertThat(typeCRUD.findOne(type.getId()).getCategory().equals("NEW")).isTrue();
    }

    @Test
    public void deleteType() {
        List<Type> types = Utils.createRandomTypes(typesCount);
        types.forEach(x -> typeCRUD.save(x));
        types.forEach(x -> typeCRUD.delete(x));
        assertThat(Collections.disjoint(typeCRUD.findAll(), types)).isTrue();
    }

}
