package pti.test.server.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import pti.test.model.authorization.Users;
import pti.test.server.interfaces.UserEngine;

import javax.faces.bean.ManagedBean;
import java.util.List;

/**
 * This class is used for registered users list reflection.
 * @author Syrotyuk R.
 */
@ManagedBean
@RestController
@Scope("view")
public class UsersBean {

    @Autowired
    private UserEngine userEngine;

    private List<Users> users;
    private Users selectedUser;

    /**
     * @return users list obtained directly from DB.
     */
    public List<Users> getUsers() {
        return userEngine.findAllUsers();
    }

    public Users getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(Users selectedUser) {
        this.selectedUser = selectedUser;
    }

}
