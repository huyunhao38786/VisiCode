package VisiCode.Controllers;

import VisiCode.Domain.*;
import VisiCode.Domain.Exceptions.UserException;
import VisiCode.Payload.LoginRequest;
import VisiCode.Payload.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebTestClient
class UserControllerTest {

    public static final String USERNAME = "user1";
    public static final String PASSWORD = "pass1234";
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController userController;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    private static final String PLACEHOLDER_PASSWORD = "actual passwords on datastore are encoded!";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void userCreatePass() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(USERNAME);
        signupRequest.setPassword(PASSWORD);
        Optional<User> user = Optional.empty();
        String str = objectWriter.writeValueAsString(signupRequest);

        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(user);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/user/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(str))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", nullValue()));
    }

    @Test
    public void userCreateFail() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(USERNAME);
        signupRequest.setPassword(PASSWORD);
        Optional<User> user = Optional.of(new User(USERNAME, "powaiefjpoaiew", new HashSet<>()));
        String str = objectWriter.writeValueAsString(signupRequest);

        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(user);

        NestedServletException e = assertThrows(NestedServletException.class, () -> {
                    mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/user/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(str));
                }
        );

        assert(e.getCause() instanceof UserException);
    }

    @Test
    public void userLoginPass() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(USERNAME);
        loginRequest.setPassword(PASSWORD);

        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(new User(USERNAME, PLACEHOLDER_PASSWORD, new HashSet<>())));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectWriter.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", nullValue()));
    }

    @Test
    void userLoginFail() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(USERNAME);
        loginRequest.setPassword(PASSWORD);

        Authentication authentication = mock(Authentication.class);
        authentication.setAuthenticated(true);

        AuthenticationException exception= mock(AuthenticationException.class);

        Mockito.when(authenticationManager.authenticate(argThat((token)->true))).thenThrow(exception);

        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(new User(USERNAME, PLACEHOLDER_PASSWORD, new HashSet<>())));

        NestedServletException e = assertThrows(NestedServletException.class, ()->{
            mockMvc.perform(
                            MockMvcRequestBuilders
                                    .post("/api/user/login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectWriter.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.error", nullValue()));
        });

        assert(e.getCause() instanceof AuthenticationException);
    }

}