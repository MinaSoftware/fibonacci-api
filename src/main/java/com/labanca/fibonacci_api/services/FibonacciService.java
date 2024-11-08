package com.labanca.fibonacci_api.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FibonacciService {

    public ResponseEntity<String> getFibonacci(int position) {

        if( position < 0) {
            return new ResponseEntity<>("Negative position not allowed", HttpStatus.PRECONDITION_FAILED);
        }
        Integer result = calculateFibonacci(position);

        // ToDo find in cache, y save

        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }

    private int calculateFibonacci(int position) {
        int previous = 0;
        int current = position <= 1 ? position : 1;

        for (int i = 2; i <= position; i++) {
            int next = previous + current;
            previous = current;
            current = next;
        }
        return current;
    }
}
