package com.labanca.fibonacci_api.controllers;

import com.labanca.fibonacci_api.services.FibonacciService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FibonacciControllerTest {

    @InjectMocks
    private FibonacciController fibonacciController;

    @Mock
    private FibonacciService fibonacciServiceMock;

    @Test
    void testGetFibonacciOk() {
        long position = 5;
        String expectedFibonacciValue = "5";
        HttpStatus expectedHttpStatus = HttpStatus.OK;

        doReturn(new ResponseEntity<>((expectedFibonacciValue), expectedHttpStatus))
                .when(fibonacciServiceMock).getFibonacci(position);

        ResponseEntity<String> response = fibonacciController.getFibonacci(position);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedFibonacciValue, response.getBody());
        assertEquals(expectedHttpStatus, response.getStatusCode());

        verify(fibonacciServiceMock, times(1)).getFibonacci(position);
    }

    @Test
    void testGetFibonacciPreconditionNegative() {
        long position = -5;
        String expectedFibonacciValue = "Negative position not allowed";
        HttpStatus expectedHttpStatus = HttpStatus.PRECONDITION_FAILED;

        doReturn(new ResponseEntity<>((expectedFibonacciValue), expectedHttpStatus))
                .when(fibonacciServiceMock).getFibonacci(position);

        ResponseEntity<String> response = fibonacciController.getFibonacci(position);

        assertEquals(HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
        assertEquals(expectedFibonacciValue, response.getBody());
        assertEquals(expectedHttpStatus, response.getStatusCode());

        verify(fibonacciServiceMock, times(1)).getFibonacci(position);
    }
}
