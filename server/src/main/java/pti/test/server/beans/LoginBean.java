package pti.test.server.beans;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import pti.test.server.JSFMessages;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * This class is responsible for authorization.
 */
@ManagedBean
@RestController
@Scope("view")
public class LoginBean {

    @Autowired
    private Logger logger;

    /**
     * Method <code>checkLoginErrors</code> processes authentication event and verifies on accordant errors.
     *
     * @param event parameter received from login page.
     */
    public void checkLoginErrors(ComponentSystemEvent event) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/shop.xhtml");
            } catch (IOException e) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Enter all of fields.", ""));
            }
        } else {
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
                    .get("SPRING_SECURITY_LAST_EXCEPTION") != null) {
                JSFMessages.error("User or password incorrect.");
            }
        }

    }

    public String doLogin() throws ServletException, IOException {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        RequestDispatcher dispatcher = ((ServletRequest) externalContext.getRequest()).getRequestDispatcher("/login");
        dispatcher.forward((ServletRequest) externalContext.getRequest(), (ServletResponse) externalContext.getResponse());
        facesContext.responseComplete();
        logger.info("Login event.");
        return null;

    }

}