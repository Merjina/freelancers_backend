package com.example.athul.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.athul.model.Feedback;
;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
