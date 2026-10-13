package com.teamforge.teammatch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:teamforge;MODE=MySQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void loginCreatesSessionThatCanAccessProtectedApi() throws Exception {
        String body = objectMapper.writeValueAsString(new Credentials("DEMO@TEAMFORGE.DEV", "teamforge123"));
        MvcResult login = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();

        MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);
        mockMvc.perform(get("/api/members").session(session))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(10));
    }

    @Test
    void invalidCredentialsAreRejected() throws Exception {
        String body = objectMapper.writeValueAsString(new Credentials("demo@teamforge.dev", "wrong-password"));
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password."));
    }

        @Test
        void registrationCreatesAccountAndStartsSession() throws Exception {
        String body = objectMapper.writeValueAsString(new Registration("Taylor Morgan", "taylor.morgan@teamforge.dev", "strongpass123"));
        MvcResult registration = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.user.name").value("Taylor Morgan"))
            .andReturn();

        MockHttpSession session = (MockHttpSession) registration.getRequest().getSession(false);
        mockMvc.perform(get("/api/auth/me").session(session))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.email").value("taylor.morgan@teamforge.dev"));
        }

        @Test
        void registrationRejectsDuplicateEmail() throws Exception {
        String body = objectMapper.writeValueAsString(new Registration("Taylor Morgan", "demo@teamforge.dev", "strongpass123"));
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isConflict());
        }

        @Test
        void registrationRejectsPasswordsShorterThanEightCharacters() throws Exception {
        String body = objectMapper.writeValueAsString(new Registration("Taylor Morgan", "taylor.short@teamforge.dev", "short"));
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
        }

    @Test
    void protectedApiRejectsUnauthenticatedRequests() throws Exception {
        mockMvc.perform(get("/api/members"))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void healthEndpointsReturnUpStatus() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void recommendationReturnsMatchedTeamWhenAuthenticated() throws Exception {
        String loginBody = objectMapper.writeValueAsString(new Credentials("DEMO@TEAMFORGE.DEV", "teamforge123"));
        MvcResult login = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);

        String brief = """
            {
                "name": "Cloud Dashboard",
                "description": "Building modern React & Spring Boot app",
                "requiredSkills": ["React", "Spring Boot", "Docker"],
                "teamSize": 3,
                "priority": "balanced"
            }
        """;

        mockMvc.perform(post("/api/projects/recommend")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(brief))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.members.length()").value(3))
                .andExpect(jsonPath("$.coverage").isNumber())
                .andExpect(jsonPath("$.averageMatch").isNumber());
    }

    @Test
    void logoutClearsSession() throws Exception {
        String loginBody = objectMapper.writeValueAsString(new Credentials("DEMO@TEAMFORGE.DEV", "teamforge123"));
        MvcResult login = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);

        mockMvc.perform(post("/api/auth/logout").session(session))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/members").session(session))
                .andExpect(status().is4xxClientError());
    }

    private record Credentials(String email, String password) {}
    private record Registration(String name, String email, String password) {}
}