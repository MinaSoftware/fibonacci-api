package com.labanca.fibonacci_api.models;

import jakarta.persistence.*;

@Entity
public class FibonacciResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long position;
    private Long result;

    public void setPosition(Long position) {
        this.position = position;
    }

    public long getResult() {
        return result;
    }

    public void setResult(long result) {
        this.result = result;
    }
}
