package pti.test.controller;

import pti.test.model.Type;

import java.util.List;

/**
 * The interface for <code>Type</code> controller.
 *
 * @author Syrotyuk R.
 */
public interface TypeController {

    /**
     * Finds all types from database.
     *
     * @return all types from database
     */
    List<Type> findAll();

    /**
     * Finds the type with specified subtype.
     *
     * @param subtype specified subtype
     * @return the type with specified subtype
     */
    Type typeByCategory(String subtype);

    /**
     * Saves the type to database.
     *
     * @param type type to be created
     */
    void save(Type type);

}
