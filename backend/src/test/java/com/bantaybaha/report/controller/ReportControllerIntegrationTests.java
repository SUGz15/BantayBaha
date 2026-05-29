package com.bantaybaha.report.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bantaybaha.project.entity.Project;
import com.bantaybaha.project.entity.ProjectStatus;
import com.bantaybaha.project.repository.ProjectRepository;
import com.bantaybaha.report.repository.ReportRepository;
import com.bantaybaha.user.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReportControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    private Project project;

    @BeforeEach
    void setUp() {
        reportRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        project = projectRepository.save(new Project(
                "Pasig River Pumping Station Upgrade",
                "Pump replacement and floodgate rehabilitation.",
                "Delta Floodworks",
                new BigDecimal("18400000.00"),
                ProjectStatus.ONGOING,
                new BigDecimal("14.5733000"),
                new BigDecimal("121.0594000"),
                LocalDate.of(2025, 3, 15),
                LocalDate.of(2026, 1, 31)
        ));
    }

    @Test
    void submitReportRequiresAuthentication() throws Exception {
        mockMvc.perform(post("/api/projects/{projectId}/reports", project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validReportJson()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticatedCitizenCanSubmitReport() throws Exception {
        String token = registerCitizen("reporter@example.com");

        mockMvc.perform(post("/api/projects/{projectId}/reports", project.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validReportJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Report submitted for review"))
                .andExpect(jsonPath("$.data.projectId").value(project.getId()))
                .andExpect(jsonPath("$.data.status").value("PENDING"))
                .andExpect(jsonPath("$.data.verificationStatus").value("INCOMPLETE"))
                .andExpect(jsonPath("$.data.verified").value(false));
    }

    @Test
    void projectReportsArePubliclyReadable() throws Exception {
        String token = registerCitizen("reader@example.com");
        submitReport(token);

        mockMvc.perform(get("/api/projects/{projectId}/reports", project.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].description").value("Project appears incomplete on site."))
                .andExpect(jsonPath("$.data[0].reporterName").value("Test Citizen"));
    }

    @Test
    void authenticatedCitizenCanViewOwnReports() throws Exception {
        String token = registerCitizen("mine@example.com");
        submitReport(token);

        mockMvc.perform(get("/api/reports/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].projectId").value(project.getId()));
    }

    @Test
    void submitReportReturnsNotFoundForMissingProject() throws Exception {
        String token = registerCitizen("missing@example.com");

        mockMvc.perform(post("/api/projects/{projectId}/reports", 9999L)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validReportJson()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Project not found"));
    }

    private void submitReport(String token) throws Exception {
        mockMvc.perform(post("/api/projects/{projectId}/reports", project.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validReportJson()))
                .andExpect(status().isCreated());
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

    private String validReportJson() {
        return """
                {
                  "verificationStatus": "INCOMPLETE",
                  "description": "Project appears incomplete on site.",
                  "imageUrl": "https://res.cloudinary.com/demo/image/upload/report.jpg",
                  "latitude": 14.5735000,
                  "longitude": 121.0599000
                }
                """;
    }
}
