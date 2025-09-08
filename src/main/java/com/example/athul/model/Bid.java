package com.example.athul.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)  // Store the enum as a string in the database
    private BidStatus status; // Enum type for status

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(nullable = false)
    private String freelancerName;

    @Column(nullable = false)
    private String freelancerEmail;

    @Column(nullable = false)
    private String clientEmail;

    @Column(nullable = false)
    private Double bidAmount;

    @CreationTimestamp
    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    public Bid() {}

    public Bid(Project project, String freelancerName, String freelancerEmail, String clientEmail, Double bidAmount) {
        this.project = project;
        this.freelancerName = freelancerName;
        this.freelancerEmail = freelancerEmail;
        this.clientEmail = clientEmail;
        this.bidAmount = bidAmount;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BidStatus getStatus() { return status; }
    public void setStatus(BidStatus status) { this.status = status; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public String getFreelancerName() { return freelancerName; }
    public void setFreelancerName(String freelancerName) { this.freelancerName = freelancerName; }

    public String getFreelancerEmail() { return freelancerEmail; }
    public void setFreelancerEmail(String freelancerEmail) { this.freelancerEmail = freelancerEmail; }

    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }

    public Double getBidAmount() { return bidAmount; }
    public void setBidAmount(Double bidAmount) { this.bidAmount = bidAmount; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
