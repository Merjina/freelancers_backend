package com.example.athul.controller;

import com.example.athul.model.Bid;
import com.example.athul.service.BidService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bids")
@CrossOrigin
public class BidController {

    private final BidService bidService;

    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    // Place a bid
    @PostMapping("/place")
    public Bid placeBid(@RequestBody Bid bid) {
        return bidService.placeBid(bid);
    }

    // Get bids by client email
    @GetMapping("/by-client")
    public List<Bid> getBidsByClientEmail(@RequestParam String email) {
        return bidService.getBidsByClientEmail(email);
    }

    // Get bids by project ID
    @GetMapping("/project/{projectId}")
    public List<Bid> getBidsByProjectId(@PathVariable Long projectId) {
        return bidService.getBidsByProjectId(projectId);
    }

    // Get bids by freelancer email with optional status filter
    @GetMapping("/by-freelancer")
    public List<Bid> getBidsByFreelancer(@RequestParam String email,
                                         @RequestParam(required = false) String status) {
        if (status != null) {
            return bidService.getBidsByFreelancerWithStatus(email, status);
        }
        return bidService.getBidsByFreelancer(email);
    }

    // Update bid status
    @PutMapping("/{bidId}/status")
    public ResponseEntity<?> updateBidStatus(@PathVariable Long bidId, @RequestParam String status) {
        bidService.updateBidStatus(bidId, status);
        return ResponseEntity.ok("Status updated");
    }
}
