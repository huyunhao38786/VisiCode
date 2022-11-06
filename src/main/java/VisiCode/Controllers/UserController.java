package VisiCode.Controllers;

import VisiCode.Domain.*;
import VisiCode.Domain.Exceptions.UserException;
import VisiCode.Payload.LoginRequest;
import VisiCode.Payload.MessageResponse;
import VisiCode.Payload.SignupRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashSet;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController extends UserAuthenticable {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/login")
    public MessageResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return MessageResponse.makeMessage("User log in successfully!");
    }

    @PostMapping("/create")
    public MessageResponse registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        userRepository.findByUsername(signupRequest.getUsername())
                .orElseThrow(() -> UserException.duplicateUser(signupRequest.getUsername()));

        // Create new user's account
        User user = new User(signupRequest.getUsername(), encoder.encode(signupRequest.getPassword()), new HashSet<>());
        userRepository.save(user);
        return MessageResponse.makeMessage("User registered successfully!");
    }
}
