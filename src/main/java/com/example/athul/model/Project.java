package com.example.athul.model;

import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Double budget;
    private String deadline;

    @Column(nullable = false)
    private String clientEmail; // The client's email

    public Project() {}

    public Project(String title, String description, Double budget, String deadline, String clientEmail) {
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.deadline = deadline;
        this.clientEmail = clientEmail;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }

	public String getFreelancerEmail() {
		// TODO Auto-generated method stub
		return null;
	}
}
