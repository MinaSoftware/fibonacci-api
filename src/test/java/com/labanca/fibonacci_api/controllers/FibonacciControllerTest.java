package com.labanca.fibonacci_api.controllers;

import com.labanca.fibonacci_api.exceptions.DataSaveException;
import com.labanca.fibonacci_api.models.FibonacciResponse;
import com.labanca.fibonacci_api.services.FibonacciService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    void testGetFibonacciOk() throws DataSaveException {
        long position = 5;
        long expectedFibonacciValue = 5;
        HttpStatus expectedHttpStatus = HttpStatus.OK;
        FibonacciResponse responseExpected = new FibonacciResponse(position, expectedFibonacciValue);

        doReturn(responseExpected)
                .when(fibonacciServiceMock).getFibonacci(position);

        FibonacciResponse response = fibonacciController.getFibonacci(position);

        assertEquals(position, response.getPosition());
        assertEquals(expectedFibonacciValue, response.getResult());
        assertEquals(expectedHttpStatus, response.getHttpStatusCode());

        verify(fibonacciServiceMock, times(1)).getFibonacci(position);
    }

    @Test
    void testGetFibonacciPreconditionNegative() throws DataSaveException {
        long position = -5;
        String expectedFibonacciValue = "Negative position not allowed";
        HttpStatus expectedHttpStatus = HttpStatus.PRECONDITION_FAILED;
        FibonacciResponse responseExpected = new FibonacciResponse(expectedFibonacciValue, expectedHttpStatus);
        doReturn(responseExpected)
                .when(fibonacciServiceMock).getFibonacci(position);

        FibonacciResponse response = fibonacciController.getFibonacci(position);

        assertEquals(responseExpected.getHttpStatusCode(), response.getHttpStatusCode());
        assertEquals(responseExpected.getError(), response.getError());

        verify(fibonacciServiceMock, times(1)).getFibonacci(position);
    }
}
