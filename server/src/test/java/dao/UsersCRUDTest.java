package dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pti.test.dao.authorization.UsersCRUD;
import pti.test.model.authorization.Users;
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
public class UsersCRUDTest {

    @Autowired
    private UsersCRUD usersCRUD;

    private int usersCount = 50;

    @Test
    public void addUser() {
        long before = usersCRUD.count();
        List<Users> users = Utils.createRandomUsers(usersCount);
        users.forEach(x -> usersCRUD.save(x));
        assertThat(usersCRUD.findAll().size() - before == usersCount).isTrue();
    }

    @Test
    public void updateUser() {
        Users user = Utils.createRandomUsers(1).get(0);
        user.setName("USER1234567890");
        usersCRUD.save(user);
        user.setName("0987654321USER");
        usersCRUD.save(user);
        assertThat(usersCRUD.findOne(user.getId()).getName().equals("0987654321USER")).isTrue();
    }

    @Test
    public void deleteUser() {
        List<Users> users = Utils.createRandomUsers(usersCount);
        users.forEach(x -> usersCRUD.save(x));
        users.forEach(x->usersCRUD.delete(x));
        assertThat(Collections.disjoint(usersCRUD.findAll(), users)).isTrue();
    }

}
