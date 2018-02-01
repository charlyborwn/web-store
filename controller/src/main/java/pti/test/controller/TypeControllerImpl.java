package pti.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pti.test.model.Type;
import pti.test.service.TypeService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * <code>TYPE</code> controller.
 */
@RestController
@RequestMapping(value = "/type")
public class TypeControllerImpl implements TypeController {

    @Autowired
    private TypeService typeService;

    @RequestMapping(value = "/findAll/add", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    public void save(@RequestBody Type type) {
        typeService.save(type);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public List<Type> findAll() {
        return typeService.findAll();
    }

    @RequestMapping(value = "/findAll/{concrete}", method = RequestMethod.GET)
    public Type typeByCategory(@PathVariable(value = "concrete") String category) {
        return typeService.findFirstByCategory(category);
    }

}
