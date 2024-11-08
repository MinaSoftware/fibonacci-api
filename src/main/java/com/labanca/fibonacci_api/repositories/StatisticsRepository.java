package com.labanca.fibonacci_api.repositories;

import com.labanca.fibonacci_api.models.FibonacciStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<FibonacciStatistics, Long> {
    Optional<FibonacciStatistics> findByPosition(long position);
}