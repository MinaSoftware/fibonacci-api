package com.labanca.fibonacci_api.services;

import com.labanca.fibonacci_api.exceptions.DataSaveException;
import com.labanca.fibonacci_api.models.FibonacciResponse;
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
    void testGetFibonacciFindInCache() throws DataSaveException {
        long position = 5;
        long expectedFibonacciResult = 5;

        FibonacciResult cachedResult = new FibonacciResult();
        cachedResult.setPosition(position);
        cachedResult.setResult(expectedFibonacciResult);
        FibonacciStatistics fibonacciStatistics = new FibonacciStatistics();
        fibonacciStatistics.setPosition(position);
        fibonacciStatistics.setCount(2);
        when(dataResultRepositoryMock.findByPosition(position)).thenReturn(Optional.of(cachedResult));

        FibonacciResponse response = fibonacciService.getFibonacci(position);

        assertEquals(HttpStatus.OK, response.getHttpStatusCode());
        assertEquals(position, response.getPosition());
        assertEquals(expectedFibonacciResult, response.getResult());

        verify(statisticsRepositoryMock, times(1)).save(any(FibonacciStatistics.class));
        verify(dataResultRepositoryMock, times(0)).save(any(FibonacciResult.class));
    }

    @Test
    void testGetFibonacciNotFindInCache() throws DataSaveException {
        long position = 5;
        long expectedFibonacciResult = 5;

        when(dataResultRepositoryMock.findByPosition(position)).thenReturn(Optional.empty());

        FibonacciResponse response = fibonacciService.getFibonacci(position);

        assertEquals(HttpStatus.OK, response.getHttpStatusCode());
        assertEquals(position, response.getPosition());
        assertEquals(expectedFibonacciResult, response.getResult());
        assertEquals(expectedFibonacciResult, response.getResult());

        verify(dataResultRepositoryMock, times(1)).save(any(FibonacciResult.class));
    }

    @Test
    void testGetFibonacciPosition1() throws DataSaveException {
        long position = 1;
        long expectedFibonacciResult = 1;

        when(dataResultRepositoryMock.findByPosition(position)).thenReturn(Optional.empty());

        FibonacciResponse response = fibonacciService.getFibonacci(position);

        assertEquals(HttpStatus.OK, response.getHttpStatusCode());
        assertEquals(position, response.getPosition());
        assertEquals(expectedFibonacciResult, response.getResult());

        verify(dataResultRepositoryMock, times(1)).save(any(FibonacciResult.class));
    }


    @Test
    void testGetFibonacciWithNegativePosition() throws DataSaveException {
        long position = -5;

        FibonacciResponse response = fibonacciService.getFibonacci(position);

        assertEquals(position, response.getPosition());
        assertEquals(HttpStatus.PRECONDITION_FAILED, response.getHttpStatusCode());
        assertEquals("Negative position not allowed", response.getError());

        verify(dataResultRepositoryMock, times(0)).findByPosition(anyLong());
        verify(dataResultRepositoryMock, times(0)).save(any(FibonacciResult.class));
    }

    @Test
    void testGetFibonacciPreconditionGreaterThan91() throws DataSaveException {
        long position = 92;
        String expectedFibonacciValue = "Positions greater than 91 are not allowed";
        HttpStatus expectedHttpStatus = HttpStatus.PRECONDITION_FAILED;

        FibonacciResponse response = fibonacciService.getFibonacci(position);

        assertEquals(position, response.getPosition());
        assertEquals(HttpStatus.PRECONDITION_FAILED, response.getHttpStatusCode());
        assertEquals(expectedFibonacciValue, response.getError());

        verify(dataResultRepositoryMock, times(0)).findByPosition(position);
    }
}
