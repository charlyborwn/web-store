package pti.test.service.authorization;


import pti.test.model.authorization.Users;

import java.util.List;

/**
 * The interface for service class for users.
 *
 * @author Syrotyuk R.
 */
public interface UserService {

    /**
     * Gets all entities from users table in DB.
     *
     * @return all entities from users table in DB
     */
    List<Users> findAll();

    /**
     * Gets only user with certain mail.
     *
     * @param mail user login
     * @return only items with certain mail
     */
    Users findByMail(String mail);

    /**
     * Creates new entity and saves it to DB and updates the existing entity.
     *
     * @param u certain user
     * @return saved or updated entity
     */
    Users save(Users u);

    /**
     * Obtains the existence of user in database by his mail/login.
     *
     * @param mail user mail/login
     * @return true if user exists, false in other case
     */
    boolean existsByMail(String mail);

}
