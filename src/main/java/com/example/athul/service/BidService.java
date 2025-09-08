package com.example.athul.service;

import com.example.athul.dto.BidDTO;
import com.example.athul.dto.ProjectDTO;
import com.example.athul.model.Bid;
import com.example.athul.model.BidStatus;
import com.example.athul.model.Project;
import com.example.athul.repository.BidRepository;
import com.example.athul.repository.ProjectRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BidService {

    private final BidRepository bidRepository;
    private final ProjectRepository projectRepository;

    public BidService(BidRepository bidRepository, ProjectRepository projectRepository) {
        this.bidRepository = bidRepository;
        this.projectRepository = projectRepository;
    }

    public Bid placeBid(Bid bid) {
        if (bid.getProject() == null || bid.getProject().getId() == null) {
            throw new IllegalArgumentException("Project not provided in the bid");
        }

        Optional<Project> optionalProject = projectRepository.findById(bid.getProject().getId());
        if (!optionalProject.isPresent()) {
            throw new IllegalArgumentException("Project not found with ID: " + bid.getProject().getId());
        }

        Project project = optionalProject.get();
        bid.setClientEmail(project.getClientEmail());

        if (bid.getFreelancerEmail() == null || bid.getFreelancerEmail().isEmpty()) {
            throw new IllegalArgumentException("Freelancer email is required");
        }

        bid.setProject(project); // Ensure full project is set

        return bidRepository.save(bid);
    }

    public List<Bid> getBidsByClientEmail(String clientEmail) {
        return bidRepository.findByClientEmail(clientEmail);
    }

    public List<Bid> getBidsByProjectId(Long projectId) {
        return bidRepository.findByProjectId(projectId);
    }

    public List<Bid> getBidsByFreelancer(String freelancerEmail) {
        return bidRepository.findByFreelancerEmail(freelancerEmail);
    }

    public List<Bid> getBidsByFreelancerWithStatus(String freelancerEmail, String status) {
        return bidRepository.findByFreelancerEmailAndStatus(freelancerEmail, status);
    }

    public void updateBidStatus(Long bidId, String status) {
        System.out.println("Updating bid ID: " + bidId + " to status: " + status);

        try {
            // Convert string status to BidStatus enum
            BidStatus bidStatus = BidStatus.valueOf(status.toUpperCase());

            Optional<Bid> optionalBid = bidRepository.findById(bidId);
            if (optionalBid.isPresent()) {
                Bid bid = optionalBid.get();
                bid.setStatus(bidStatus);
                bidRepository.save(bid);
                System.out.println("Bid updated successfully.");
            } else {
                throw new RuntimeException("Bid not found with id: " + bidId);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status provided: " + status);
        }
    }

    public List<BidDTO> getBidsForFreelancer(String freelancerEmail) {
        List<Bid> bids = bidRepository.findByFreelancerEmail(freelancerEmail);
        List<BidDTO> bidDTOs = new ArrayList<>();

        for (Bid bid : bids) {
            BidDTO bidDTO = new BidDTO();
            bidDTO.setId(bid.getId());
            bidDTO.setBidAmount(bid.getBidAmount());

            // Convert BidStatus enum to String and set to bidDTO
            if (bid.getStatus() != null) {
                bidDTO.setStatus(bid.getStatus().name());  // Convert to String using name()
            }

            bidDTO.setTimestamp(bid.getTimestamp());

            Project project = bid.getProject();
            if (project != null) {
                ProjectDTO projectDTO = new ProjectDTO();
                projectDTO.setId(project.getId());
                projectDTO.setTitle(project.getTitle());
                projectDTO.setDescription(project.getDescription());
                bidDTO.setProject(projectDTO);
            }

            bidDTOs.add(bidDTO);
        }

        return bidDTOs;
    }

}
