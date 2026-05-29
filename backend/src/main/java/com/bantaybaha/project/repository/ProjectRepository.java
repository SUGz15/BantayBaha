package com.bantaybaha.project.repository;

import com.bantaybaha.project.entity.Project;
import com.bantaybaha.project.entity.ProjectStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("""
            select project
            from Project project
            where (:status is null or project.status = :status)
              and (
                :search is null
                or lower(project.title) like lower(concat('%', :search, '%'))
                or lower(project.description) like lower(concat('%', :search, '%'))
                or lower(project.contractor) like lower(concat('%', :search, '%'))
              )
            order by project.title asc
            """)
    List<Project> findMapProjects(
            @Param("status") ProjectStatus status,
            @Param("search") String search
    );
}
