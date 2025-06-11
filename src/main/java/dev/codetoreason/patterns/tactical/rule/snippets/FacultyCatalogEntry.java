package dev.codetoreason.patterns.tactical.rule.snippets;

import dev.codetoreason.patterns.tactical.infra.repository.Entity;
import lombok.Builder;

@Builder
record FacultyCatalogEntry(
        FacultyId id,
        String name,
        FieldsOfStudies fieldsOfStudies
) implements Entity<FacultyId> {

    int fieldsOfStudiesNumber() {
        return fieldsOfStudies.count();
    }
}
