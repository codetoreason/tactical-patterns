package dev.codetoreason.patterns.tactical.result.snippets;

import java.util.Optional;

class Result<T> {

    private final T value;
    private final String message;

    private Result(T value, String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        this.value = value;
        this.message = message;
    }

    static <T> Result<T> successful(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null for successful result");
        }
        return new Result<>(value, "OK");
    }

    static <T> Result<T> failed(String message) {
        return new Result<>(null, message);
    }

    Optional<T> value() {
        return Optional.ofNullable(value);
    }

    boolean isSuccess() {
        return value != null;
    }

    boolean isFailure() {
        return !isSuccess();
    }

    String message() {
        return message;
    }

}
