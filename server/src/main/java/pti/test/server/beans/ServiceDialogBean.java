package pti.test.server.beans;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pti.test.server.JSFMessages;
import pti.test.server.interfaces.UserEngine;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * This class is responsible for user's dialog form
 * processing. Secured by <code>SpringSecurity</code>.
 *
 * @author Syrotyuk R.
 */

@ManagedBean
@RestController
@ViewScoped
public class ServiceDialogBean {

    @Autowired
    private UserEngine userEngine;

    @Autowired
    private Logger logger;

    private String message;
    private String choice;
    private String productProblem;
    private String deliveringProblem;
    private String phone;
    private String mail;
    private String city;

    /**
     * Checks the correctness of dialog form filling and sends
     * user's message to service.
     */
    public void send() {

        if (message == null || message.equals("")) {
            JSFMessages.error("Enter your message.", "Please, fill all forms.");
            return;
        } else if (choice.equals("Product") & (productProblem == null || productProblem.equals(""))) {
            JSFMessages.error("Enter product ID or name.", "Please, fill all forms.");
            return;
        } else if (choice.equals("Delivering") & (deliveringProblem == null || deliveringProblem.equals(""))) {
            JSFMessages.error("Enter your order number.", "Please, fill all forms.");
            return;
        } else if (phone.equals("") | (mail.equals("") | !mail.contains("@")) | city.equals("")) {
            JSFMessages.error("Enter all forms correctly.", "Please, fill all forms.");
            return;
        } else {
            JSFMessages.info("Dear " + userEngine.getUser().getName() + "! Thanks for your appeal!",
                    "We will contact to you as soon as possible.");
        }
        logger.info("User message.");
        allClear();

    }

    /**
     * Obtains current user's name.
     * @return current user's name
     */
//    private String getSystemUserName() {
//        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Users u = userService.findByMail(user.getUsername());
//        return u.getName();
//    }

    /**
     * Sets all dialog form fields to <code>null</code> or makes its empty.
     */
    private void allClear() {
        message = "";
        productProblem = "";
        phone = "";
        mail = "";
        deliveringProblem = "";
        choice = null;
        city = "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getProductProblem() {
        return productProblem;
    }

    public void setProductProblem(String productProblem) {
        this.productProblem = productProblem;
    }

    public String getDeliveringProblem() {
        return deliveringProblem;
    }

    public void setDeliveringProblem(String deliveringProblem) {
        this.deliveringProblem = deliveringProblem;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
