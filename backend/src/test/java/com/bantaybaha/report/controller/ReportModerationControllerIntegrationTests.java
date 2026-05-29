package com.bantaybaha.report.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bantaybaha.auth.security.JwtService;
import com.bantaybaha.project.entity.Project;
import com.bantaybaha.project.entity.ProjectStatus;
import com.bantaybaha.project.repository.ProjectRepository;
import com.bantaybaha.report.entity.Report;
import com.bantaybaha.report.entity.VerificationStatus;
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
class ReportModerationControllerIntegrationTests {

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

    private Report report;

    @BeforeEach
    void setUp() {
        reportRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();

        Project project = projectRepository.save(new Project(
                "San Juan Creek Floodwall",
                "Creek floodwall and retaining wall construction.",
                "Metro Flood Builders",
                new BigDecimal("9600000.00"),
                ProjectStatus.ONGOING,
                new BigDecimal("14.6042000"),
                new BigDecimal("121.0293000"),
                LocalDate.of(2025, 4, 1),
                LocalDate.of(2025, 11, 30)
        ));

        User citizen = userRepository.save(new User(
                "Report Owner",
                "owner@example.com",
                passwordEncoder.encode("password123"),
                Role.CITIZEN
        ));

        report = reportRepository.save(new Report(
                project,
                citizen,
                VerificationStatus.DAMAGED,
                "Floodwall has visible cracks after recent rain.",
                "https://res.cloudinary.com/demo/image/upload/cracks.jpg",
                new BigDecimal("14.6045000"),
                new BigDecimal("121.0297000")
        ));
    }

    @Test
    void pendingReportsRequireAdminRole() throws Exception {
        String citizenToken = registerCitizen("viewer@example.com");

        mockMvc.perform(get("/api/admin/reports/pending")
                        .header("Authorization", "Bearer " + citizenToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanListPendingReports() throws Exception {
        String adminToken = createAdminToken();

        mockMvc.perform(get("/api/admin/reports/pending")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id").value(report.getId()))
                .andExpect(jsonPath("$.data[0].status").value("PENDING"));
    }

    @Test
    void adminCanApproveReport() throws Exception {
        String adminToken = createAdminToken();

        mockMvc.perform(post("/api/admin/reports/{reportId}/approve", report.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "reason": "Photo evidence is clear."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Report approved"))
                .andExpect(jsonPath("$.data.status").value("APPROVED"))
                .andExpect(jsonPath("$.data.verified").value(true))
                .andExpect(jsonPath("$.data.moderationReason").value("Photo evidence is clear."))
                .andExpect(jsonPath("$.data.moderatedById").exists())
                .andExpect(jsonPath("$.data.moderatedAt").exists());
    }

    @Test
    void adminCanRejectReport() throws Exception {
        String adminToken = createAdminToken();

        mockMvc.perform(post("/api/admin/reports/{reportId}/reject", report.getId())
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "reason": "Image does not match project location."
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Report rejected"))
                .andExpect(jsonPath("$.data.status").value("REJECTED"))
                .andExpect(jsonPath("$.data.verified").value(false))
                .andExpect(jsonPath("$.data.moderationReason").value("Image does not match project location."));
    }

    @Test
    void moderationReturnsNotFoundForMissingReport() throws Exception {
        String adminToken = createAdminToken();

        mockMvc.perform(post("/api/admin/reports/{reportId}/approve", 9999L)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Report not found"));
    }

    private String createAdminToken() {
        User admin = userRepository.save(new User(
                "Admin User",
                "admin-%s@example.com".formatted(System.nanoTime()),
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
}
