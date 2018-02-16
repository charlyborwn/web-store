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
 * @author Syrotyuk R.
 */
@ManagedBean
@RestController
@ViewScoped
public class TypeEngineImpl implements TypeEngine{

    @Autowired
    private TypeController typeController;

    private static HashMap<String, Type> types = new HashMap<>();

    @Override
    public List<Type> getTypeList() {
        if (types.size() == 0) {
            typeController.findAll().forEach(x -> types.put(x.getCategory(), x));
        }
        return new ArrayList<>(types.values());
    }

    @Override
    public void addType(Type type) {
        typeController.save(type);
        types.put(type.getCategory(), type);
    }

    @Override
    public Type getTypeByCategory(String category) {
        Type type = types.get(category);
        if (type == null) {
            return typeController.typeByCategory(category);
        }
        return type;
    }

}
