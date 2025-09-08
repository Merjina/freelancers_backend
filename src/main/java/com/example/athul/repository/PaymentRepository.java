package com.example.athul.repository;

import com.example.athul.model.Payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	  List<Payment> findByFreelancerId(Long freelancerId);  // Assuming freelancer is the client

}
