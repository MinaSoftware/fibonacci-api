package com.labanca.fibonacci_api.controllers;

import com.labanca.fibonacci_api.exceptions.DataSaveException;
import com.labanca.fibonacci_api.models.FibonacciResponse;
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
    public FibonacciResponse getFibonacci(@PathVariable long position) throws DataSaveException {
        return fibonacciService.getFibonacci(position);
    }
}