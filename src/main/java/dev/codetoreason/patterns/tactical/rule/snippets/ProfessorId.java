package dev.codetoreason.patterns.tactical.rule.snippets;

import java.util.UUID;

record ProfessorId(
        UUID value
) {

    static ProfessorId newOne() {
        return new ProfessorId(UUID.randomUUID());
    }
}
