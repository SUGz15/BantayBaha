package com.bantaybaha.project.controller;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bantaybaha.auth.security.JwtService;
import com.bantaybaha.project.entity.Project;
import com.bantaybaha.project.entity.ProjectStatus;
import com.bantaybaha.project.repository.ProjectRepository;
import com.bantaybaha.report.repository.ReportRepository;
import com.bantaybaha.user.entity.Role;
import com.bantaybaha.user.entity.User;
import com.bantaybaha.user.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectAdminControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private Project project;

    @BeforeEach
    void setUp() {
        reportRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        project = projectRepository.save(new Project(
                "Existing Flood Control Project",
                "Existing project description.",
                "Existing Contractor",
                new BigDecimal("5000000.00"),
                ProjectStatus.PLANNED,
                new BigDecimal("14.5000000"),
                new BigDecimal("121.0000000"),
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 12, 1)
        ));
    }

    @Test
    void citizenCannotCreateProject() throws Exception {
        String citizenToken = registerCitizen("project-citizen@example.com");

        mockMvc.perform(post("/api/admin/projects")
                        .header("Authorization", "Bearer " + citizenToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validProjectJson("Citizen Attempt")))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanCreateProject() throws Exception {
        String adminToken = createAdminToken();

        mockMvc.perform(post("/api/admin/projects")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validProjectJson("Tondo Pumping Station Rehab")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Project created"))
                .andExpect(jsonPath("$.data.title").value("Tondo Pumping Station Rehab"))
                .andExpect(jsonPath("$.data.status").value("ONGOING"))
                .andExpect(jsonPath("$.data.latitude").value(14.6173000))
                .andExpect(jsonPath("$.data.longitude").value(120.9671000));
    }

    @Test
    void adminCanUpdateProject() throws Exception {
        String adminToken = createAdminToken();

        mockMvc.perform(put("/api/admin/projects/{id}", project.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validProjectJson("Updated Flood Control Project")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Project updated"))
                .andExpect(jsonPath("$.data.id").value(project.getId()))
                .andExpect(jsonPath("$.data.title").value("Updated Flood Control Project"))
                .andExpect(jsonPath("$.data.status").value("ONGOING"));
    }

    @Test
    void updateProjectReturnsNotFound() throws Exception {
        String adminToken = createAdminToken();

        mockMvc.perform(put("/api/admin/projects/{id}", 9999L)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validProjectJson("Missing Project")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Project not found"));
    }

    @Test
    void createProjectValidatesRequest() throws Exception {
        String adminToken = createAdminToken();

        mockMvc.perform(post("/api/admin/projects")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "",
                                  "description": "Missing important fields",
                                  "contractor": "",
                                  "budget": -1,
                                  "status": "ONGOING",
                                  "latitude": 100,
                                  "longitude": 200
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.validationErrors.title").exists())
                .andExpect(jsonPath("$.validationErrors.contractor").exists())
                .andExpect(jsonPath("$.validationErrors.budget").exists())
                .andExpect(jsonPath("$.validationErrors.latitude").exists())
                .andExpect(jsonPath("$.validationErrors.longitude").exists());
    }

    private String createAdminToken() {
        User admin = userRepository.save(new User(
                "Project Admin",
                "project-admin-%s@example.com".formatted(System.nanoTime()),
                passwordEncoder.encode("password123"),
                Role.ADMIN
        ));

        return jwtService.generateToken(admin);
    }

    private String registerCitizen(String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fullName": "Test Citizen",
                                  "email": "%s",
                                  "password": "password123"
                                }
                                """.formatted(email)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.token", not(blankOrNullString())))
                .andReturn();

        return com.jayway.jsonpath.JsonPath.read(result.getResponse().getContentAsString(), "$.data.token");
    }

    private String validProjectJson(String title) {
        return """
                {
                  "title": "%s",
                  "description": "Rehabilitation of pumping equipment and nearby flood barriers.",
                  "contractor": "Harbor Civil Works",
                  "budget": 14250000.00,
                  "status": "ONGOING",
                  "latitude": 14.6173000,
                  "longitude": 120.9671000,
                  "startDate": "2025-05-01",
                  "targetCompletionDate": "2025-12-20"
                }
                """.formatted(title);
    }
}
