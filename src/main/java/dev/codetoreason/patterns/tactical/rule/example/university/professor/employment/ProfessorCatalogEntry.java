package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment;

import dev.codetoreason.patterns.tactical.infra.repository.Entity;

record ProfessorCatalogEntry(
        ProfessorId id,
        String name,
        FieldsOfStudies fieldsOfStudies
) implements Entity<ProfessorId> {
}
