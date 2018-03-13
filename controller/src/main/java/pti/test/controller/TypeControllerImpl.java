package pti.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pti.test.model.Type;
import pti.test.service.TypeService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * <code>Type</code> controller.
 *
 * @author Syrotyuk R.
 */
@RestController
@RequestMapping(value = "/type")
public class TypeControllerImpl implements TypeController {

    @Autowired
    private TypeService typeService;

    /**
     * Obtains the request at defined URL and finds all types
     * from database.
     *
     * @return all types from database
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Type> findAll() {
        return typeService.findAll();
    }

    /**
     * Obtains the request at defined URL and finds the type
     * with specified subtype.
     *
     * @param subtype specified subtype
     * @return the type with specified subtype
     */
    @RequestMapping(value = "/{subtype}", method = RequestMethod.GET)
    public Type typeByCategory(@PathVariable(value = "subtype") String subtype) {
        return typeService.findFirstByCategory(subtype);
    }

    /**
     * Obtains the request at defined URL and saves the type to database.
     *
     * @param type type to be created
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    public void save(@RequestBody Type type) {
        typeService.save(type);
    }

}
