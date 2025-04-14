package dev.codetoreason.patterns.tactical.result.experimental.lombok;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Accessors(fluent = true)
@RequiredArgsConstructor(access = PRIVATE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
@EqualsAndHashCode
@ToString
class Result<T> {

    T value;
    @Getter
    String message;

    static <T> Result<T> successful(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null for successful result");
        }
        return new Result<>(value, "OK");
    }

    static <T> Result<T> failed(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        return new Result<>(null, message);
    }

    static <T> Result<T> failed(String messageTemplate, Object... args) {
        return failed(messageTemplate.formatted(args));
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
}
