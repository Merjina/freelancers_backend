package com.example.athul.controller;

import com.example.athul.model.Project;
import com.example.athul.security.JwtUtil;
import com.example.athul.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:3000") // Enable CORS for React
public class ProjectController {

    private final ProjectService projectService;
    private final JwtUtil jwtUtil;

    // ✅ Constructor with both dependencies
    public ProjectController(ProjectService projectService, JwtUtil jwtUtil) {
        this.projectService = projectService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    public ResponseEntity<Project> addProject(
            @RequestBody Project project,
            @RequestHeader("Authorization") String token
    ) {
        String jwt = token.substring(7); // Remove "Bearer "
        String clientEmail = jwtUtil.extractUsername(jwt); // Extract email from JWT

        project.setClientEmail(clientEmail); // Set client email before saving
        Project savedProject = projectService.addProject(project);

        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody Project updatedProject) {
        Optional<Project> existingProject = projectService.findById(id);

        if (existingProject.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Project not found");
        }

        Project updated = projectService.updateProject(id, updatedProject);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }

    @GetMapping("/client/{clientEmail}")
    public List<Project> getProjectsByClientEmail(@PathVariable String clientEmail) {
        return projectService.getProjectsByClientEmail(clientEmail);
    }
}