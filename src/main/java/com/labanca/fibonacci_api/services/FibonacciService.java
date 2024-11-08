package com.labanca.fibonacci_api.services;

import com.labanca.fibonacci_api.models.FibonacciResult;
import com.labanca.fibonacci_api.models.FibonacciStatistics;
import com.labanca.fibonacci_api.repositories.DataResultRepository;
import com.labanca.fibonacci_api.repositories.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FibonacciService {

    @Autowired
    private DataResultRepository dataResultRepository;

    @Autowired
    private StatisticsRepository statisticsRepository;

    public ResponseEntity<String> getFibonacci(long position) {

        // Precondition: position debe ser un entero positivo menor a 92
        if (position < 0) {
            return new ResponseEntity<>("Negative position not allowed", HttpStatus.PRECONDITION_FAILED);
        } else if (position > 91){
            return new ResponseEntity<>("Positions greater than 91 are not allowed", HttpStatus.PRECONDITION_FAILED);
        }

        Long resultInCache = findInCache(position);

        Long result = resultInCache != null ? resultInCache : calculateFibonacci(position);

        updateStatistics(position);

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    private Long findInCache(long position) {
        Optional<FibonacciResult> cachedResult = dataResultRepository.findByPosition(position);
        return cachedResult.isPresent() ? cachedResult.get().getResult() : null;
    }

    private long calculateFibonacci(long position) {
        long previous = 0;
        long current = position <= 1 ? position : 1;

        for (long i = 2; i <= position; i++) {
            long next = previous + current;
            previous = current;
            current = next;
        }
        saveInDataBase(position, current);
        return current;
    }

    private void saveInDataBase(long position, long fibonacciNumber) {
        try {
            FibonacciResult result = new FibonacciResult();
            result.setPosition(position);
            result.setResult(fibonacciNumber);
            dataResultRepository.save(result);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save Fibonacci result in the database", e);
        }
    }

    private void updateStatistics(long position) {
        try {
            FibonacciStatistics stats = statisticsRepository.findById(position)
                    .orElse(new FibonacciStatistics());
            stats.setPosition(position);
            Long count = stats.getCount() != null ? stats.getCount() : 0;
            stats.setCount(count + 1);
            statisticsRepository.save(stats);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save Fibonacci Statistics in the database", e);
        }
    }
}
