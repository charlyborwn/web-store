package pti.test.dao.authorization;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pti.test.model.authorization.Users;

import java.util.List;

/**
 * This interface contains a methods for working with DB and
 * extends <code>CrudRepository</code> that allow to be confined
 * to only methods declaration.
 */
@Repository
public interface UsersCRUD extends CrudRepository<Users, Long> {

    /**
     * Gets all entities from users table in DB.
     *
     * @return all entities from users table in DB
     */
    @Cacheable({"users1"})
    List<Users> findAll();

    /**
     * Gets only user with certain mail.
     *
     * @param mail user login
     * @return only items with certain mail
     */
    @Cacheable({"users2"})
    Users findByMail(String mail);

    /**
     * Creates new entity and saves it to DB and updates the existing entity.
     *
     * @param u certain user
     * @return saved or updated entity
     */
    @CacheEvict(value = {"users1", "users2"}, key = "#p0.id", allEntries = true)
    Users save(Users u);

    @Cacheable({"users2"})
    boolean existsByMail(String mail);

}
