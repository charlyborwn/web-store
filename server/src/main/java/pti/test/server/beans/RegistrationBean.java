package pti.test.server.beans;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import pti.test.model.authorization.Users;
import pti.test.server.JSFMessages;
import pti.test.server.interfaces.UserEngine;
import pti.test.service.authorization.UserService;

import javax.faces.bean.ManagedBean;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is responsible for user's registration process.
 * @author Syrotyuk R.
 */

@ManagedBean
@RestController
@Scope("view")
public class RegistrationBean {

    @Autowired
    private UserEngine userEngine;

    @Autowired
    private Logger logger;

    private Users user;

    @Size(min = 3, max = 12)
    private String name;

    @Size(min = 3, max = 12)
    private String surname;

    @Pattern(regexp = "^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[a-zA-Z]{2,4}$", message = "Not correct email")
    private String mail;

    @Size(min = 5, max = 15, message = "Wrong size for password")
    private String password;

    /**
     * Adds user's data to BD if all registration fields are completely filled
     * and if no user with same email/login is currently present. Password is
     * encrypted with salt.
     */
    public void register() {

        if (!name.equals("") & !surname.equals("") & !mail.equals("") & !password.equals("")) {
            if (userEngine.existsByMail(mail)) {
                JSFMessages.error("User " + mail + " already exist.");
                return;
            }
            user = new Users();
            user.setName(name);
            user.setSurname(surname);
            user.setMail(mail);
            user.setRole("USER");
            user.setPassword((new BCryptPasswordEncoder()).encode(password));
            user.setUserhistory(new HashMap<>());
            user.setFavourites(new ArrayList<>());
            user.setProducts(new HashMap<>());
            user.setUsertemp(new HashMap<>());
            userEngine.saveUser(user);
            logger.info("Registration successful.");
            JSFMessages.info("Registration successful.");
        } else {
            JSFMessages.error("Enter all of fields.");
        }

    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
