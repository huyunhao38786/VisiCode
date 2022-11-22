package VisiCode.Controllers;

import VisiCode.Domain.*;
import VisiCode.Domain.Exceptions.UserException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebTestClient
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void userCreatePass() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("user1");
        signupRequest.setPassword("pass1234");
        Optional<User> user = Optional.empty();
        String str = objectWriter.writeValueAsString(signupRequest);

        Mockito.when(userRepository.findByUsername("user1")).thenReturn(user);

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
        signupRequest.setUsername("user1");
        signupRequest.setPassword("pass1234");
        Optional<User> user = Optional.of(new User("user1", "powaiefjpoaiew", new HashSet<>()));
        String str = objectWriter.writeValueAsString(signupRequest);

        Mockito.when(userRepository.findByUsername("user1")).thenReturn(user);

        NestedServletException e = assertThrows(NestedServletException.class, () -> {
                    mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/user/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(str));
                }
        );

        assert(e.getCause() instanceof UserException);
    }
}