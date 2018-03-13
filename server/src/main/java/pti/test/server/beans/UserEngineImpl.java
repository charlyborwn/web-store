package pti.test.server.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import pti.test.model.StoredProduct;
import pti.test.model.authorization.Users;
import pti.test.server.interfaces.UserEngine;
import pti.test.service.authorization.UserService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class holds all user operations methods and passes its
 * to service level.
 *
 * @author Syrotyuk R.
 */
@ManagedBean
@RestController
@ViewScoped
public class UserEngineImpl implements UserEngine {

    @Autowired
    private UserService userService;

    private static ConcurrentHashMap<String, Users> users = new ConcurrentHashMap<>();

    /**
     * Returns the current user, obtains all users from database and puts
     * theirs into synchronized HashMap to increase the site velocity.
     *
     * @return current user authorities as UserDetails object
     */
    @Override
    public Users getUser() {
        if (users.size() == 0) {
            userService.findAll().forEach(x -> users.put(x.getMail(), x));
        }
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = details.getUsername();
            if (users.get(username) != null) {
                return users.get(username);
            } else {
                users.put(username, userService.findByMail(details.getUsername()));
                return users.get(username);
            }
        } else {
            return null;
        }
    }

    /**
     * Updates currant user.
     */
    @Override
    public void saveUser() {
        Users user = getUser();
        userService.save(user);
        users.put(user.getMail(), user);
    }

    /**
     * Saves a new user or updates the existing one.
     *
     * @param user user instance to be saved or updated
     */
    @Override
    public void saveUser(Users user) {
        userService.save(user);
        users.put(user.getMail(), user);
    }

    /**
     * Gets all entities from users table in DB.
     *
     * @return all entities from users table in DB
     */
    @Override
    public List<Users> findAllUsers() {
        return userService.findAll();
    }

    /**
     * Gets only user with certain mail.
     *
     * @param mail user login
     * @return only items with certain mail
     */
    @Override
    public Users findUserByMail(String mail) {
        if (users.size() == 0 | users.get(mail) == null) {
            users.put(mail, userService.findByMail(mail));
            return userService.findByMail(mail);
        }
        return users.get(mail);
    }

    /**
     * Sets the user favourites.
     *
     * @param favourites
     */
    @Override
    public void setUserFavourites(ArrayList<Long> favourites) {
        Users user = getUser();
        user.setFavourites(favourites);
        users.put(user.getMail(), user);
    }

    /**
     * Sets the user history.
     *
     * @param history
     */
    @Override
    public void setUserHistory(HashMap<LocalDateTime, List<StoredProduct>> history) {
        Users user = getUser();
        user.setUserhistory(history);
        users.put(user.getMail(), user);
    }

    /**
     * Sets the user current orders.
     *
     * @param temp
     */
    @Override
    public void setUserTemp(HashMap<LocalDateTime, List<StoredProduct>> temp) {
        Users user = getUser();
        user.setUsertemp(temp);
        users.put(user.getMail(), user);
    }

    /**
     * Sets the user cart.
     *
     * @param cart
     */
    @Override
    public void setUserCart(HashMap<Long, Integer> cart) {
        Users user = getUser();
        user.setProducts(cart);
        users.put(user.getMail(), user);
    }

    /**
     * Obtains current user name.
     *
     * @return current user name
     */
    @Override
    public String getUserName() {
        return getUser().getName();
    }

    /**
     * Obtains the user existence by his login/mail.
     *
     * @param mail user login/mail
     * @return true is user with specified mail exists, false in
     * other case
     */
    @Override
    public boolean existsByMail(String mail) {
        return userService.existsByMail(mail);
    }

}
