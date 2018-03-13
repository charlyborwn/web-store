package pti.test.dao;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pti.test.model.Type;

import java.util.List;

/**
 * This interface contains a methods for working with DB and
 * extends <code>CrudRepository</code> that allow to be confined
 * to only methods declaration.
 *
 * @author Syrotyuk R.
 */
@Repository
public interface TypeCRUD extends CrudRepository<Type, Long> {

    /**
     * Gets all entities from types table in DB.
     *
     * @return all entities from types table in DB
     */
    @Cacheable({"types"})
    List<Type> findAll();

    /**
     * Creates new entity and saves it to DB and updates the existing entity.
     *
     * @param t certain type
     * @return saved or updated entity
     */
    @CacheEvict(value = {"types"}, key = "#p0.id", allEntries = true)
    Type save(Type t);

    /**
     * Finds the first entity with certain category field.
     *
     * @param category category field
     * @return the first entity with certain category field
     */
    Type findFirstByCategory(String category);

}
