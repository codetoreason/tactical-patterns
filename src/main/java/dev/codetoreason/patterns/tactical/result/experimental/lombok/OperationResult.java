package dev.codetoreason.patterns.tactical.result.experimental.lombok;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor(access = PRIVATE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
@EqualsAndHashCode
@ToString
class OperationResult {

    boolean isSuccess;
    String message;

    private static final OperationResult SUCCESSFUL = new OperationResult(true, "OK");

    static OperationResult successful() {
        return SUCCESSFUL;
    }

    static OperationResult failed(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
        return new OperationResult(false, message);
    }

    static OperationResult failed(String messageTemplate, Object... args) {
        return failed(messageTemplate.formatted(args));
    }

    boolean isFailure() {
        return !isSuccess;
    }
}
