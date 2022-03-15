package com.example.jpabook.domain.exception;

public class NotEnoughStockException extends RuntimeException {
    // 맨위쪽 싹다 override

    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }
}
