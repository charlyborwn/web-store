package pti.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pti.test.dao.TypeCRUD;
import pti.test.model.Type;

import java.util.List;

/**
 * This class is responsible for requests delegation from controller level to DAO level.
 *
 * @author Syrotyuk R.
 */
@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeCRUD typeCRUD;

    /**
     * Gets all entities from types table in DB.
     *
     * @return all entities from types table in DB
     */
    @Override
    public List<Type> findAll() {
        return typeCRUD.findAll();
    }

    /**
     * Creates new entity and saves it to DB and updates the existing entity.
     *
     * @param t certain type
     * @return saved or updated entity
     */
    @Override
    public Type save(Type t) {
        return typeCRUD.save(t);
    }

    /**
     * Finds the first entity with certain category field.
     *
     * @param category category field
     * @return the first entity with certain category field
     */
    @Override
    public Type findFirstByCategory(String category) {
        return typeCRUD.findFirstByCategory(category);
    }
}
