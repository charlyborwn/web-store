package pti.test.server.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pti.test.controller.TypeController;
import pti.test.model.Type;
import pti.test.server.interfaces.TypeEngine;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is responsible for all types operation delegation
 * from Spring managed beans to controller level and holding
 * the types in HashMap to increase the application velocity.
 *
 * @author Syrotyuk R.
 */
@ManagedBean
@RestController
@ViewScoped
public class TypeEngineImpl implements TypeEngine {

    @Autowired
    private TypeController typeController;

    private static HashMap<String, Type> types = new HashMap<>();

    /**
     * Obtains the list of all products and puts them into
     * the holding types HashMap.
     *
     * @return the list of all products
     */
    @Override
    public List<Type> getTypeList() {
        if (types.size() == 0) {
            typeController.findAll().forEach(x -> types.put(x.getCategory(), x));
        }
        return new ArrayList<>(types.values());
    }

    /**
     * Obtains the type by specified subtype from the holding types HashMap.
     * If such type is not exist then finds it in the database. If there will
     * be no such type then returns null.
     *
     * @param subtype specified subtype to find its type
     * @return the type by specified subtype if exists or null if not.
     */
    @Override
    public Type getTypeByCategory(String subtype) {
        Type type = types.get(subtype);
        if (type == null) {
            return typeController.typeByCategory(subtype);
        }
        return type;
    }

    /**
     * Saves new type to the holding types HashMap and to database.
     *
     * @param type type entity to be saved
     */
    @Override
    public void addType(Type type) {
        typeController.save(type);
        types.put(type.getCategory(), type);
    }

}
