package com.example.athul.repository;

import com.example.athul.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findByFreelancerEmail(String freelancerEmail);

    List<Bid> findByFreelancerName(String freelancerName);

    List<Bid> findByClientEmail(String clientEmail);

    List<Bid> findByProjectId(Long projectId);
    List<Bid> findByFreelancerEmailAndStatus(String email, String status);
    @Query("SELECT b FROM Bid b JOIN FETCH b.project WHERE b.freelancerEmail = :email")
    List<Bid> findBidsWithProjectByFreelancerEmail(@Param("email") String email);

}
