package com.labanca.fibonacci_api.services;

import com.labanca.fibonacci_api.exceptions.DataSaveException;
import com.labanca.fibonacci_api.exceptions.StatisticsSaveException;
import com.labanca.fibonacci_api.models.FibonacciResponse;
import com.labanca.fibonacci_api.models.FibonacciResult;
import com.labanca.fibonacci_api.models.FibonacciStatistics;
import com.labanca.fibonacci_api.repositories.DataResultRepository;
import com.labanca.fibonacci_api.repositories.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FibonacciService {

    private static final long MIN_POSITION = 0;
    private static final long MAX_POSITION = 91;

    @Autowired
    private DataResultRepository dataResultRepository;

    @Autowired
    private StatisticsRepository statisticsRepository;

    public FibonacciResponse getFibonacci(long position) throws DataSaveException {

        // Precondition: position debe ser un entero positivo menor a 92
        if (position < MIN_POSITION) {
            return new FibonacciResponse("Negative position not allowed", HttpStatus.PRECONDITION_FAILED);
        } else if (position > MAX_POSITION){
                return new FibonacciResponse("Positions greater than 91 are not allowed", HttpStatus.PRECONDITION_FAILED);
        }

        Long resultInCache = findInCache(position);

        Long result = resultInCache != null ? resultInCache : calculateFibonacci(position);

        updateStatistics(position);

        return new FibonacciResponse(position, result);
    }

    private Long findInCache(long position) {
        Optional<FibonacciResult> cachedResult = dataResultRepository.findByPosition(position);
        return cachedResult.isPresent() ? cachedResult.get().getResult() : null;
    }

    private long calculateFibonacci(long position) throws DataSaveException {
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

    private void saveInDataBase(long position, long fibonacciNumber) throws DataSaveException {
        try {
            FibonacciResult result = new FibonacciResult();
            result.setPosition(position);
            result.setResult(fibonacciNumber);
            dataResultRepository.save(result);
        } catch (Exception e) {
            throw new DataSaveException("Failed to save Fibonacci result in the database", e.getCause());
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
            throw new StatisticsSaveException("Failed to save Fibonacci Statistics in the database", e.getCause());
        }
    }
}
