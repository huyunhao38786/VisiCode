package VisiCode.Controllers;

import VisiCode.Domain.Exceptions.UserException;
import VisiCode.Domain.User;
import VisiCode.Domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

public class UserAuthenticable {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    protected User getAuthenticated(Authentication auth) {
        if (auth == null) throw UserException.noUser(null);
        String username = auth.getName();
        return userRepository.findByUsername(username).orElseThrow(() -> UserException.noUser(username));
    }
}
