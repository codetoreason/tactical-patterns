package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment;

import java.util.UUID;

public record ProfessorId(
        UUID value
) {

    public static ProfessorId newOne() {
        return new ProfessorId(UUID.randomUUID());
    }
}
