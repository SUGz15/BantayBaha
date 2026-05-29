package com.bantaybaha.project.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bantaybaha.project.entity.Project;
import com.bantaybaha.project.entity.ProjectStatus;
import com.bantaybaha.project.repository.ProjectRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    private Project completedProject;

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();

        completedProject = projectRepository.save(new Project(
                "Marikina River Floodwall Segment A",
                "Concrete floodwall works near a residential riverbank.",
                "Bayan Builders Corporation",
                new BigDecimal("12500000.00"),
                ProjectStatus.COMPLETED,
                new BigDecimal("14.6507000"),
                new BigDecimal("121.1029000"),
                LocalDate.of(2025, 1, 10),
                LocalDate.of(2025, 8, 30)
        ));

        projectRepository.save(new Project(
                "Caloocan Drainage Upgrade",
                "Drainage canal widening and culvert replacement.",
                "North Metro Infrastructure",
                new BigDecimal("8700000.00"),
                ProjectStatus.ONGOING,
                new BigDecimal("14.7566000"),
                new BigDecimal("121.0450000"),
                LocalDate.of(2025, 2, 1),
                LocalDate.of(2025, 12, 15)
        ));
    }

    @Test
    void getMapProjectsReturnsPublicMarkers() throws Exception {
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].title").value("Caloocan Drainage Upgrade"))
                .andExpect(jsonPath("$.data[0].latitude").value(14.7566000))
                .andExpect(jsonPath("$.data[0].longitude").value(121.0450000));
    }

    @Test
    void getMapProjectsCanFilterByStatus() throws Exception {
        mockMvc.perform(get("/api/projects").param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].status").value("COMPLETED"));
    }

    @Test
    void getMapProjectsCanSearchText() throws Exception {
        mockMvc.perform(get("/api/projects").param("search", "river"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].title").value("Marikina River Floodwall Segment A"));
    }

    @Test
    void getProjectReturnsDetails() throws Exception {
        mockMvc.perform(get("/api/projects/{id}", completedProject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Marikina River Floodwall Segment A"))
                .andExpect(jsonPath("$.data.contractor").value("Bayan Builders Corporation"))
                .andExpect(jsonPath("$.data.budget").value(12500000.00))
                .andExpect(jsonPath("$.data.startDate").value("2025-01-10"))
                .andExpect(jsonPath("$.data.targetCompletionDate").value("2025-08-30"));
    }

    @Test
    void getProjectReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/projects/{id}", 9999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Project not found"));
    }
}
