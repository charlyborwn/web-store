package pti.test.service;

import pti.test.model.Type;

import java.util.List;

/**
 * The interface for service class for product types.
 *
 * @author Syrotyuk R.
 */
public interface TypeService {

    /**
     * Gets all entities from types table in DB.
     *
     * @return all entities from types table in DB
     */
    List<Type> findAll();

    /**
     * Creates new entity and saves it to DB and updates the existing entity.
     *
     * @param t certain type
     * @return saved or updated entity
     */
    Type save(Type t);

    /**
     * Finds the first entity with certain category field.
     *
     * @param category category field
     * @return the first entity with certain category field
     */
    Type findFirstByCategory(String category);

}
