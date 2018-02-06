package pti.test.service.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pti.test.dao.authorization.UsersCRUD;
import pti.test.model.authorization.Users;

import java.util.List;

/**
 * This class is responsible for requests delegation from controller level to DAO level.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersCRUD usersCRUD;

    @Override
    public List<Users> findAll() {
        return usersCRUD.findAll();
    }

    @Override
    public Users findByMail(String mail) {
        return usersCRUD.findByMail(mail);
    }

    @Override
    public Users save(Users u) {
        return usersCRUD.save(u);
    }

    @Override
    public boolean existsByMail(String mail) {
        return usersCRUD.existsByMail(mail);
    }
}
