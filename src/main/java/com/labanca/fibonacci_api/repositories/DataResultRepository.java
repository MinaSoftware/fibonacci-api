package com.labanca.fibonacci_api.repositories;

import com.labanca.fibonacci_api.models.FibonacciResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataResultRepository extends JpaRepository<FibonacciResult, Long> {
    Optional<FibonacciResult> findByPosition(int position);
}