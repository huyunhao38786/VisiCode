package VisiCode.Controllers;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
class ReactAppControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void get() throws Exception {
        String index = mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getForwardedUrl();
        assertEquals(index, "/index.html");
    }
}