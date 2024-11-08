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

    public ResponseEntity<String> getFibonacci(int position) {

        if( position < 0) {
            return new ResponseEntity<>("Negative position not allowed", HttpStatus.PRECONDITION_FAILED);
        }
        Integer resultInCache = findInCache(position);

        Integer result =  resultInCache != null ? resultInCache : calculateFibonacci(position);

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    private Integer findInCache(int position) {
        Optional<FibonacciResult> cachedResult = dataResultRepository.findByPosition(position);
        return cachedResult.isPresent() ? cachedResult.get().getResult() : null;
    }

    private int calculateFibonacci(int position) {
        int previous = 0;
        int current = position <= 1 ? position : 1;

        for (int i = 2; i <= position; i++) {
            int next = previous + current;
            previous = current;
            current = next;
        }
        saveInDataBase(position, current);
        return current;
    }

    private void saveInDataBase(int position, int fibonacciNumber) {
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
