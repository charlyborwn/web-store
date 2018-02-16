package pti.test.server.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import pti.test.model.StoredProduct;
import pti.test.model.authorization.Users;
import pti.test.server.interfaces.UserEngine;
import pti.test.service.authorization.UserService;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Syrotyuk R.
 */
@ManagedBean
@RestController
@ViewScoped
public class UserEngineImpl implements UserEngine {

    @Autowired
    private UserService userService;

    private static ConcurrentHashMap<String, Users> users = new ConcurrentHashMap<>();

    @Override
    public Users getUser() {
        if (users.size() == 0) {
            userService.findAll().stream().forEach(x -> users.put(x.getMail(), x));
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

    @Override
    public void saveUser() {
        Users user = getUser();
        userService.save(user);
        users.put(user.getMail(), user);
    }

    @Override
    public void saveUser(Users user) {
        userService.save(user);
        users.put(user.getMail(), user);
    }

    @Override
    public List<Users> findAllUsers() {
        return userService.findAll();
    }

    @Override
    public Users findUserByMail(String mail) {
        if (users.size() == 0 | users.get(mail) == null) {
            users.put(mail, userService.findByMail(mail));
            return userService.findByMail(mail);
        }
        return users.get(mail);
    }

    @Override
    public void setUserFavourites(ArrayList<Long> favourites) {
        Users user = getUser();
        user.setFavourites(favourites);
        users.put(user.getMail(), user);
    }

    @Override
    public void setUserHistory(HashMap<LocalDateTime, List<StoredProduct>> history) {
        Users user = getUser();
        user.setUserhistory(history);
        users.put(user.getMail(), user);
    }

    @Override
    public void setUserTemp(HashMap<LocalDateTime, List<StoredProduct>> temp) {
        Users user = getUser();
        user.setUsertemp(temp);
        users.put(user.getMail(), user);
    }

    @Override
    public void setUserCart(HashMap<Long, Integer> cart) {
        Users user = getUser();
        user.setProducts(cart);
        users.put(user.getMail(), user);
    }

    @Override
    public String getUserName() {
        return getUser().getName();
    }

    @Override
    public boolean existsByMail(String mail) {
        return userService.existsByMail(mail);
    }
}
