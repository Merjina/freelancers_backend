package com.example.athul.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.athul.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	  List<Project> findByClientEmail(String clientEmail); // 👈 This is needed for bid filtering
	   Optional<Project> findById(Long id);  // Make sure this exists

}
