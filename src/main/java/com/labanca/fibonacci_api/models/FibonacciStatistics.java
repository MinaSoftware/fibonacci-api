package com.labanca.fibonacci_api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class FibonacciStatistics {

    @Id
    private Long position;
    private Long count;

    public void setPosition(long position) {
        this.position = position;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
