package com.labanca.fibonacci_api.services;

import com.labanca.fibonacci_api.models.FibonacciResult;
import com.labanca.fibonacci_api.repositories.DataResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FibonacciService {

    @Autowired
    private DataResultRepository dataResultRepository;

    public ResponseEntity<String> getFibonacci(Long position) {

        if( position < 0) {
            return new ResponseEntity<>("Negative position not allowed", HttpStatus.PRECONDITION_FAILED);
        }
        Long resultInCache = findInCache(position);

        Long result =  resultInCache != null ? resultInCache : calculateFibonacci(position);

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    private Long findInCache(Long position) {
        Optional<FibonacciResult> cachedResult = dataResultRepository.findByPosition(position);
        return cachedResult.isPresent() ? cachedResult.get().getResult() : null;
    }

    private long calculateFibonacci(Long position) {
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
        } catch (Exception e){
            throw new RuntimeException("Failed to save Fibonacci result in the database", e);
        }
    }
}
