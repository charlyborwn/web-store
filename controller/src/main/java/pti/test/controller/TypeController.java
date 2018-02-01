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
public interface TypeController {

    void save(Type type);

    List<Type> findAll();

    Type typeByCategory(String category);

}
