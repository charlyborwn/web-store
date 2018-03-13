package pti.test.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * This class is responsible for redirecting to defined URLs.
 *
 * @author Syrotyuk R.
 */
@ManagedBean
@RestController
@Scope("view")
public class Redirecter {

    private final static Logger LOG = LoggerFactory.getLogger(Redirecter.class);

    public static final String PRODUCT_UPDATE = "/admin/product_update.xhtml";
    public static final String PRODUCT = "/admin/product.xhtml";
    public static final String SHOP = "../shop.xhtml";
    public static final String REGISTRATION = "registration.xhtml";
    public static final String LOGIN = "login.xhtml";
    public static final String CART = "cart.xhtml";
    public static final String ADD = "/admin/add_product.xhtml";
    public static final String ADD_TYPE = "/admin/add_types.xhtml";
    public static final String USERS = "/admin/users.xhtml";

    public static void redirectToPage(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException e) {
            LOG.warn("Can't redirect to: {}", url);
        }
    }

}
