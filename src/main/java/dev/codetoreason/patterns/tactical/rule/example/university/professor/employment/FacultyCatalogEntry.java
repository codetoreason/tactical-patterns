package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment;

import lombok.Builder;

@Builder
record FacultyCatalogEntry(
        FacultyId id,
        String name,
        FieldsOfStudies fieldsOfStudies
) {

    public int fieldsOfStudiesNumber() {
        return fieldsOfStudies.count();
    }
}
