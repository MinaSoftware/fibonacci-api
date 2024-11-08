package com.labanca.fibonacci_api.services;

import com.labanca.fibonacci_api.models.FibonacciResult;
import com.labanca.fibonacci_api.models.FibonacciStatistics;
import com.labanca.fibonacci_api.repositories.DataResultRepository;
import com.labanca.fibonacci_api.repositories.StatisticsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FibonacciServiceTest {

    @InjectMocks
    FibonacciService fibonacciService;

    @Mock
    DataResultRepository dataResultRepositoryMock;

    @Mock
    StatisticsRepository statisticsRepositoryMock;

    @Test
    void testGetFibonacciFindInCache() {
        long position = 5;
        long expectedFibonacciResult = 5;

        FibonacciResult cachedResult = new FibonacciResult();
        cachedResult.setPosition(position);
        cachedResult.setResult(expectedFibonacciResult);
        FibonacciStatistics fibonacciStatistics = new FibonacciStatistics();
        fibonacciStatistics.setPosition(position);
        fibonacciStatistics.setCount(2);
        when(dataResultRepositoryMock.findByPosition(position)).thenReturn(Optional.of(cachedResult));

        ResponseEntity<String> response = fibonacciService.getFibonacci(position);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(String.valueOf(expectedFibonacciResult), response.getBody());

        verify(statisticsRepositoryMock, times(1)).save(any(FibonacciStatistics.class));
        verify(dataResultRepositoryMock, times(0)).save(any(FibonacciResult.class));
    }

    @Test
    void testGetFibonacciNotFindInCache() {
        long position = 5;
        String expectedFibonacciResult = "5";

        when(dataResultRepositoryMock.findByPosition(position)).thenReturn(Optional.empty());

        ResponseEntity<String> response = fibonacciService.getFibonacci(position);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedFibonacciResult, response.getBody());

        verify(dataResultRepositoryMock, times(1)).save(any(FibonacciResult.class));
    }

    @Test
    void testGetFibonacciPosition1() {
        long position = 1;
        long expectedFibonacciResult = 1;

        when(dataResultRepositoryMock.findByPosition(position)).thenReturn(Optional.empty());

        ResponseEntity<String> response = fibonacciService.getFibonacci(position);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(String.valueOf(expectedFibonacciResult), response.getBody());

        verify(dataResultRepositoryMock, times(1)).save(any(FibonacciResult.class));
    }


    @Test
    void testGetFibonacciWithNegativePosition() {
        long position = -5;

        ResponseEntity<String> response = fibonacciService.getFibonacci(position);

        assertEquals(HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
        assertEquals("Negative position not allowed", response.getBody());

        verify(dataResultRepositoryMock, times(0)).findByPosition(anyLong());
        verify(dataResultRepositoryMock, times(0)).save(any(FibonacciResult.class));
    }

    @Test
    void testGetFibonacciPreconditionGreaterThan91() {
        long position = 92;
        String expectedFibonacciValue = "Positions greater than 91 are not allowed";
        HttpStatus expectedHttpStatus = HttpStatus.PRECONDITION_FAILED;

        ResponseEntity<String> response = fibonacciService.getFibonacci(position);

        assertEquals(HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
        assertEquals(expectedFibonacciValue, response.getBody());
        assertEquals(expectedHttpStatus, response.getStatusCode());

        verify(dataResultRepositoryMock, times(0)).findByPosition(position);
    }
}
