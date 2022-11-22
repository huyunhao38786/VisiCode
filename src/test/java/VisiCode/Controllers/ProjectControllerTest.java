package VisiCode.Controllers;

import VisiCode.Domain.*;
import VisiCode.Payload.ProjectCreationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.stream.StreamSupport;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class ProjectControllerTest {

    public static final String USERNAME = "test";
    private static final String EMPTY = "";
    private static final String SMALL = "0".repeat(Note.MAX_BLOB_SIZE / 1024);
    private static final String LARGE = "0".repeat(Note.MAX_BLOB_SIZE);
    private static final String OVERSIZED = "0".repeat(Note.MAX_BLOB_SIZE + 1);
    private static final String OVERLARGE = "0".repeat(Note.MAX_BLOB_SIZE * 2);

    private static final Project p1 = Project.forTest("Project 1", 1L);
    private static final Project p2 = Project.forTest("Project 2", 2L);
    private static final Project p3 = Project.forTest("Project 3", 3L);

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProjectRepository projectRepository;

    @MockBean
    NoteRepository noteRepository;

    @MockBean
    UserRepository userRepository;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(projectRepository.findById(p1.getId())).thenReturn(Optional.of(p1));
    }

    @Test
    void myProjectsUnauthorized() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/project")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(USERNAME)
    void myProjectsEmpty() throws Exception {
        when(userRepository.findByUsername(eq(USERNAME))).thenReturn(Optional.of(new User(USERNAME, "", new HashSet<>())));

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/project"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(USERNAME)
    void myProjectsOne() throws Exception {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(new User(USERNAME, "", new HashSet<>(List.of(p1.getId())))));
        when(projectRepository.findAllById(argThat(ids ->
                StreamSupport.stream(ids.spliterator(), false).anyMatch(id -> Objects.equals(id, p1.getId()))
        ))).thenReturn(List.of(p1));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/project"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasItem(p1.getName())));
    }

    @Test
    @WithMockUser(USERNAME)
    void myProjectsMany() throws Exception {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(new User(USERNAME, "", new HashSet<>())));
        when(projectRepository.findAllById(argThat(id->true))).thenReturn(List.of(p1, p2, p3));
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/api/project"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasItem(p1.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasItem(p2.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasItem(p3.getName())));
    }

    @Test
    @WithMockUser(USERNAME)
    void createProject() throws Exception {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(new User(USERNAME, "", new HashSet<>())));
        final String PROJECT_NAME = "Create one project";
        String resp = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/project/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(ProjectCreationRequest.forTest(PROJECT_NAME))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
//        Project p = objectMapper.readValue(resp, Project.class);
//        assertEquals(p.getName(), PROJECT_NAME);
//        assertEquals(p.getEditorId().length(), 36);
//        assertEquals(p.getViewerId().length(), 36);
//        assertEquals(p.getId(), -1);
//        assertEquals(p.getNotes().size(), 0);
    }

    @Test
    void removeProjectNonExistent() {
    }

    @Test
    void removeProjectUnauthorized() {

    }

    @Test
    void removeProjectValid() {

    }

    @Test
    void viewOwnProject() {
    }

    @Test
    void viewOwnProjectUnauthorized() {
    }

    @Test
    void viewOwnProjectNonExistent() {
    }

    @Test
    void viewOtherProject() {
    }

    @Test
    void addTextNoteSmall() {
    }

    @Test
    void addTextNoteLarge() {
    }

    @Test
    void addTextNoteOversized() {
    }

    @Test
    void addTextNoteOverLarge() {
    }

    @Test
    void addTextNoteNoPermission() {
    }

    @Test
    void addTextNoteEditor() {
    }

    @Test
    void addTextNoteOwner() {
    }

    @Test
    void addFileNoteSmall() {
    }

    @Test
    void addFileNoteLarge() {
    }

    @Test
    void addFileNoteOversized() {
    }

    @Test
    void addFileNoteOverLarge() {
    }

    @Test
    void addFileNoteNoPermission() {
    }

    @Test
    void addFileNoteEditor() {
    }

    @Test
    void addFileNoteOwner() {
    }

    @Test
    void removeNote() {
    }

    @Test
    void viewNote() {
    }
}