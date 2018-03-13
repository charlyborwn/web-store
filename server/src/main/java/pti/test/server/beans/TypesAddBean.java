package pti.test.server.beans;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import pti.test.model.Type;
import pti.test.server.JSFMessages;
import pti.test.server.interfaces.TypeAdd;
import pti.test.server.interfaces.TypeEngine;

import javax.faces.bean.ManagedBean;

/**
 * This class is responsible for adding new product type or category.
 *
 * @author Syrotyuk R.
 */
@ManagedBean
@RestController
@Scope("view")
public class TypesAddBean implements TypeAdd {

    @Autowired
    private TypeEngine typeEngine;

    @Autowired
    private Logger logger;

    private String type;
    private String category;

    /**
     * Validates the same category name presence and adds it to DB.
     */
    @Override
    public void addType() {
        if (typeEngine.getTypeList().stream().filter(x -> x.getCategory().equals(category)).count() > 0) {
            JSFMessages.error("Type or category already exists.");
        } else {
            if (type != "" & category != "") {
                Type t = new Type();
                t.setType(type);
                t.setCategory(category);
                typeEngine.addType(t);
                logger.info("Type " + type + " with " + category + " category successfully added.");
                JSFMessages.info("Type " + type + " with " + category + " category successfully added.");
            } else {
                JSFMessages.error("Enter all of fields.");
            }
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
