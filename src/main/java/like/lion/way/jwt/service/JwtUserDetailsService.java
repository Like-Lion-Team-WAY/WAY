package like.lion.way.jwt.service;


import like.lion.way.user.domain.User;
import like.lion.way.user.service.UserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userService.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("사용자가 없습니다.");
        }
        Hibernate.initialize(user.getRoles());
        org.springframework.security.core.userdetails.User.UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);
        userBuilder.roles(user.getRoles().stream().map(role -> role.getRoleName()).toArray(String[]::new));
        return userBuilder.build();
    }
}