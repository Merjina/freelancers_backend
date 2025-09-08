package com.example.athul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.athul.model.Project;
import com.example.athul.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project updatedProject) {
        return projectRepository.findById(id)
                .map(project -> {
                    project.setTitle(updatedProject.getTitle());
                    project.setDescription(updatedProject.getDescription());
                    project.setBudget(updatedProject.getBudget());
                    project.setDeadline(updatedProject.getDeadline());
                    return projectRepository.save(project);
                })
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public void deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new RuntimeException("Project not found");
        }
    }
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }
    public List<Project> getProjectsByClientEmail(String clientEmail) {
        return projectRepository.findByClientEmail(clientEmail);
    }

}
