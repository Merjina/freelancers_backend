package com.example.athul.controller;

import com.example.athul.dto.PaymentDTO;
import com.example.athul.dto.TransactionDTO; // Import TransactionDTO
import com.example.athul.model.Payment;
import com.example.athul.model.User;
import com.example.athul.repository.PaymentRepository;
import com.example.athul.repository.UserRepository;
import com.example.athul.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentDTO dto) {
        // Validate payment DTO
        if (!dto.isValid()) {
            return ResponseEntity.badRequest().body("Invalid payment details");
        }

        // Fetch freelancer details
        User freelancer = userRepository.findById(dto.getFreelancerId())
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));

        // Create and save payment
        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setUpiId(dto.getUpiId());
        payment.setCardNumber(dto.getCardNumber());
        payment.setCardHolderName(dto.getCardHolderName());
        payment.setBankName(dto.getBankName());
        payment.setFreelancer(freelancer);

        paymentRepository.save(payment);

        return ResponseEntity.ok("Payment saved successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPayments() {
        // Fetch all payments
        List<Payment> payments = paymentRepository.findAll();
        
        // Convert payments to DTOs
        List<PaymentDTO> paymentDTOs = payments.stream()
                .map(payment -> new PaymentDTO(
                        payment.getAmount(),
                        payment.getPaymentMethod(),
                        payment.getUpiId(),
                        payment.getCardNumber(),
                        payment.getCardHolderName(),
                        payment.getBankName(),
                        payment.getFreelancer().getId(),
                        payment.getFreelancer().getFullName() // Pass freelancer's full name
                ))
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(paymentDTOs);
    }

    @GetMapping("/history/{clientId}")
    public ResponseEntity<?> getPaymentHistory(@PathVariable Long clientId) {
        // Fetch the client by ID
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        // Fetch the payments made by this client (assuming you have a method to get payments for a client)
        List<Payment> payments = paymentRepository.findByFreelancerId(clientId);

        // Return payments and client details in the response
        PaymentHistoryResponse response = new PaymentHistoryResponse(client, payments);
        return ResponseEntity.ok(response);
    }

    public static class PaymentHistoryResponse {
        private User client;
        private List<Payment> payments;

        public PaymentHistoryResponse(User client, List<Payment> payments) {
            this.client = client;
            this.payments = payments;
        }

        public User getClient() {
            return client;
        }

        public void setClient(User client) {
            this.client = client;
        }

        public List<Payment> getPayments() {
            return payments;
        }

        public void setPayments(List<Payment> payments) {
            this.payments = payments;
        }
    }

    @GetMapping("/freelancer")
    public ResponseEntity<?> getPaymentsForFreelancer(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // Remove "Bearer "

        String email = jwtUtil.extractUsername(token);
        User freelancer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));

        List<Payment> payments = paymentRepository.findByFreelancerId(freelancer.getId());

        List<PaymentDTO> paymentDTOs = payments.stream()
                .map(payment -> new PaymentDTO(
                        payment.getAmount(),
                        payment.getPaymentMethod(),
                        payment.getUpiId(),
                        payment.getCardNumber(),
                        payment.getCardHolderName(),
                        payment.getBankName(),
                        payment.getFreelancer().getId(),
                        payment.getFreelancer().getFullName()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(paymentDTOs);
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getAllTransactionData() {
        // Fetch all payments
        List<Payment> payments = paymentRepository.findAll();

        // Convert payments to TransactionDTOs
        List<TransactionDTO> transactions = payments.stream().map(payment -> new TransactionDTO(
            payment.getId(),  // Treating this as transactionId
            payment.getFreelancer().getFullName(),
            payment.getAmount(),
            "Completed",  // Static status (or change based on business logic)
            payment.getPaymentDate(),
            payment.getPaymentMethod()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(transactions);
    }
}
