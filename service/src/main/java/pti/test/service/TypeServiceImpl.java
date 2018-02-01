package pti.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pti.test.dao.TypeCRUD;
import pti.test.model.Type;

import java.util.List;

/**
 * This class is responsible for requests delegation from controller level to DAO level.
 */
@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeCRUD typeCRUD;

    @Override
    public List<Type> findAll() {
        return typeCRUD.findAll();
    }

    @Override
    public Type save(Type t) {
        return typeCRUD.save(t);
    }

    @Override
    public Type findFirstByCategory(String category) {
        return typeCRUD.findFirstByCategory(category);
    }
}
