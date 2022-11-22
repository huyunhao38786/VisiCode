package VisiCode.Controllers;

import VisiCode.Domain.*;
import VisiCode.Payload.LoginRequest;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebTestClient
class ProjectControllerTest {

    private static final String EMPTY = "";
    private static final String SMALL = "0".repeat(Note.MAX_BLOB_SIZE / 1024);
    private static final String LARGE = "0".repeat(Note.MAX_BLOB_SIZE);
    private static final String OVERSIZED = "0".repeat(Note.MAX_BLOB_SIZE + 1);
    private static final String OVERLARGE = "0".repeat(Note.MAX_BLOB_SIZE * 2);

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        authentication = mock(Authentication.class);
        authentication.setAuthenticated(true);
    }

    @Test
    @WithMockUser()
    void myProjects() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/project")
                        .with(authentication(authentication))
        ).andExpect(status().isOk());
    }

    @Test
    void createProject() {
    }

    @Test
    void removeProject() {
    }

    @Test
    void viewOwnProject() {
    }

    @Test
    void viewOtherProject() {
    }

    @Test
    void addFileNote() {
    }

    @Test
    void addTextNote() {
    }

    @Test
    void removeNote() {
    }

    @Test
    void viewNote() {
    }
}