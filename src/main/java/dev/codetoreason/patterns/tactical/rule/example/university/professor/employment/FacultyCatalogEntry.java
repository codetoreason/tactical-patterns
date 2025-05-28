package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment;

import dev.codetoreason.patterns.tactical.infra.repository.Entity;
import lombok.Builder;

@Builder
record FacultyCatalogEntry(
        FacultyId id,
        String name,
        FieldsOfStudies fieldsOfStudies
) implements Entity<FacultyId> {

    public int fieldsOfStudiesNumber() {
        return fieldsOfStudies.count();
    }
}
