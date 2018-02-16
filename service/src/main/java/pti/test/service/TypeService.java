package pti.test.service;

import pti.test.model.Type;

import java.util.List;

/**
 * @author Syrotyuk R.
 */
public interface TypeService {

    List<Type> findAll();

    Type save(Type t);

    Type findFirstByCategory(String category);

}
