package pti.test.server;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * This class is responsible for creating of information,
 * warning and error faces messages.
 *
 * @author Syrotyuk R.
 */
public class JSFMessages {

    public static void info(String message, String details) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, message, details));
    }

    public static void info(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
    }

    public static void warn(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, message, null));
    }

    public static void warn(String message, String details) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, message, details));
    }

    public static void error(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }

    public static void error(String message, String details) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, details));
    }

}
