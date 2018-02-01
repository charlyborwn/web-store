package pti.test.service.authorization;


import pti.test.model.authorization.Users;

import java.util.List;

public interface UserService {

    List<Users> findAll();

    Users findByMail(String mail);

    Users save(Users u);

}
