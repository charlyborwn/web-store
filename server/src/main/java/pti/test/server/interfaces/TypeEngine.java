package pti.test.server.interfaces;

import pti.test.model.Type;

import java.util.List;

/**
 * This interface is responsible for all type operations.
 *
 * @author Syrotyuk R.
 */
public interface TypeEngine {

    List<Type> getTypeList();

    void addType(Type type);

    Type getTypeByCategory(String category);

}
