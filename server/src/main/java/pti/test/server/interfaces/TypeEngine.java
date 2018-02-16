package pti.test.server.interfaces;

import pti.test.model.Type;

import java.util.List;
/**
 * @author Syrotyuk R.
 */
public interface TypeEngine {

    List<Type> getTypeList();

    void addType(Type type);

    Type getTypeByCategory(String category);

}
