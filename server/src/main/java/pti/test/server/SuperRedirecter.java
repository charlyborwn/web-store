package pti.test.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.authorization.Users;
import pti.test.server.beans.SystemUserActionsBean;
import pti.test.server.interfaces.UserEngine;
import pti.test.service.authorization.UserService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.jws.soap.SOAPBinding;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@ManagedBean
@RestController
@ViewScoped
public class SuperRedirecter {

    @Autowired
    private UserEngine userEngine;

    @Autowired
    private Logger logger;

    public void toHome() {
        Redirecter.redirectToPage("shop.xhtml?user=" + getSystemUserName("name") + "&role=" + getSystemUserName("auth"));
        logger.info("Redirected to: " + "shop.xhtml");
    }

    public void toShop() {
        Redirecter.redirectToPage("../shop.xhtml?user=" + getSystemUserName("name") + "&role=" + getSystemUserName("auth"));
        logger.info("Redirected to: " + "shop.xhtml");
    }

    public void toProductPage() {
        FacesContext context = FacesContext.getCurrentInstance();
        String id = context.getExternalContext().getRequestParameterMap().get("id");
        Redirecter.redirectToPage("test.xhtml?id=" + id);
        logger.info("Redirected to: " + "test.xhtml");
    }

    public void toCart() {
        Redirecter.redirectToPage("cart.xhtml?user=" + getSystemUserName("name") + "&role=" + getSystemUserName("auth"));
        logger.info("Redirected to: " + "cart.xhtml");
    }

    public void toFavs() {
        Redirecter.redirectToPage("favourites.xhtml?user=" + getSystemUserName("name") + "&role=" + getSystemUserName("auth"));
        logger.info("Redirected to: " + "favourites.xhtml");
    }

    public void toService() {
        Redirecter.redirectToPage("dialog.xhtml?user=" + getSystemUserName("name") + "&role=" + getSystemUserName("auth"));
        logger.info("Redirected to: " + "dialog.xhtml");
    }

    public void toHistory() {
        Redirecter.redirectToPage("history.xhtml?user=" + getSystemUserName("name") + "&role=" +
                getSystemUserName("auth") + "&user_mail=" + getSystemUserName("mail"));
        logger.info("Redirected to: " + "history.xhtml");
    }

    public void toSelectedProduct(Long id) {
        Redirecter.redirectToPage("../test.xhtml?id=" + id);
        logger.info("Redirected to: " + "test.xhtml");
    }

    public void toProductList() {
        Redirecter.redirectToPage("product.xhtml");
        logger.info("Redirected to: " + "product.xhtml");
    }

    public void toAddProductPage() {
        Redirecter.redirectToPage("add_product.xhtml");
        logger.info("Redirected to: " + "add_product.xhtml");
    }

    public void toAddTypePage() {
        Redirecter.redirectToPage("add_types.xhtml");
        logger.info("Redirected to: " + "add_types.xhtml");
    }

    public void toUsers() {
        Redirecter.redirectToPage("users.xhtml");
        logger.info("Redirected to: " + "users.xhtml");
    }

    public void toUpdate(long ipk) {
        Redirecter.redirectToPage("/admin/product_update.xhtml?id=" + ipk);
        logger.info("Redirected to: " + "product_update.xhtml");
    }

    public void toAdmin() {
        Redirecter.redirectToPage(Redirecter.PRODUCT);
        logger.info("Redirected to: " + "product.xhtml");
    }

    public void toTemp() {
        Redirecter.redirectToPage("../temp.xhtml?user=" + getSystemUserName("name") + "&role=" + getSystemUserName("auth"));
        logger.info("Redirected to: " + "temp_update.xhtml");
    }

    public void toRegistration() {
        Redirecter.redirectToPage(Redirecter.REGISTRATION);
        logger.info("Redirected to: " + "registration.xhtml");
    }

    private String getSystemUserName(String u) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users byMail = userEngine.findUserByMail(user.getUsername());
        if (u.equals("name")) {
            return byMail.getName().toLowerCase();
        }
        if (u.equals("auth")) {
            if (byMail.getRole().equals("ADMIN")) {
                return "admin";
            }
            return "user";
        }
        if (u.equals("mail")) {
            return user.getUsername();
        }
        return null;
    }

}
