package pti.test.service.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pti.test.dao.authorization.UsersCRUD;
import pti.test.model.authorization.Users;

import java.util.List;

/**
 * This class is responsible for requests delegation from controller
 * level to DAO level.
 *
 * @author Syrotyuk R.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersCRUD usersCRUD;

    /**
     * Gets all entities from users table in DB.
     *
     * @return all entities from users table in DB
     */
    @Override
    public List<Users> findAll() {
        return usersCRUD.findAll();
    }

    /**
     * Gets only user with certain mail.
     *
     * @param mail user login
     * @return only items with certain mail
     */
    @Override
    public Users findByMail(String mail) {
        return usersCRUD.findByMail(mail);
    }

    /**
     * Creates new entity and saves it to DB and updates the existing entity.
     *
     * @param u certain user
     * @return saved or updated entity
     */
    @Override
    public Users save(Users u) {
        return usersCRUD.save(u);
    }

    /**
     * Obtains the existence of user in database by his mail/login.
     *
     * @param mail user mail/login
     * @return true if user exists, false in other case
     */
    @Override
    public boolean existsByMail(String mail) {
        return usersCRUD.existsByMail(mail);
    }

}
