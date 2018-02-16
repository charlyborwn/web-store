package pti.test.service.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pti.test.dao.authorization.UsersCRUD;
import pti.test.model.authorization.Users;

import java.util.List;

/**
 * This class is responsible for forming users authorities and saving its into
 * <code>UserDetails</code> object.
 * @author Syrotyuk R.
 */
@Service
public class SystemUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersCRUD usersCRUD;

    Users user;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        try {
            user = usersCRUD.findByMail(mail);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (user == null) {
            throw new UsernameNotFoundException(String.format("The user not exists.", mail));
        }
        List<GrantedAuthority> roles = AuthorityUtils.createAuthorityList("ROLE_" + user.getRole());
        UserDetails userDetails = new User(user.getMail(), user.getPassword(), roles);
        return userDetails;
    }

}