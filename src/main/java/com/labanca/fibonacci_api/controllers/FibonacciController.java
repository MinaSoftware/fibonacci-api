package com.labanca.fibonacci_api.controllers;

import com.labanca.fibonacci_api.services.FibonacciService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fibonacci")
public class FibonacciController {

    @Autowired
    private FibonacciService fibonacciService;

    @GetMapping("/{position}")
    public ResponseEntity<String> getFibonacci(@PathVariable int position) {
        return fibonacciService.getFibonacci(position);
    }
}