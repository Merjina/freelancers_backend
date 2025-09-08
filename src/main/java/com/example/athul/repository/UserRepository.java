package com.example.athul.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.athul.model.User;

@Repository
	public interface UserRepository extends JpaRepository<User, Long> {
	    Optional<User> findByEmail(String email); 
	    Optional<User> findByPhone(String phone);
	    Optional<User> findByResetToken(String resetToken);
	    
	    Optional<User> findByEmailAndRole(String email, String role);

	    
	    long countByUserType(String userType);
	    
	 // ✅ Newly added method
	    List<User> findByRole(String role);

}
