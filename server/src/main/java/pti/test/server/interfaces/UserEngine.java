package pti.test.server.interfaces;

import pti.test.model.StoredProduct;
import pti.test.model.authorization.Users;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Syrotyuk R.
 */
public interface UserEngine {

    Users getUser();

    void saveUser();

    void saveUser(Users user);

    List<Users> findAllUsers();

    Users findUserByMail(String mail);

    void setUserFavourites(ArrayList<Long> favourites);

    void setUserHistory(HashMap<LocalDateTime, List<StoredProduct>> history);

    void setUserTemp(HashMap<LocalDateTime, List<StoredProduct>> temp);

    void setUserCart(HashMap<Long, Integer> cart);

    String getUserName();

    boolean existsByMail(String mail);

}
