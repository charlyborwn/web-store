package pti.test.server.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import pti.test.controller.TypeController;
import pti.test.model.Type;
import pti.test.server.interfaces.TypeEngine;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for types and categories
 * lists forming.
 */
@ManagedBean
@RestController
@ViewScoped
public class TypeListBean {

    @Autowired
    private TypeEngine typeController;

    private List<String> types;
    private List<String> categories;
    private String type;
    private String category;

    /**
     * Creates the list of all findAll.
     *
     * @return collection of unique type names
     */
    //@PostConstruct
    public List<String> getTypes() {
        types = typeController.getTypeList().stream()
                .map(Type::getType)
                .distinct()
                .collect(Collectors.toList());
        return types;
    }

    /**
     * Listener for type change event, loads all categories that
     * chosen type has in.
     */
    public void onTypeChange(String type) {
        if (type != null && !type.equals("")) {
            categories = typeController.getTypeList().stream()
                    .filter(x -> x.getType().equals(type))
                    .map(Type::getCategory)
                    .collect(Collectors.toList());
        } else {
            categories = new LinkedList<>();
        }

    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getType() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            type = null;
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            category = null;
        }
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
