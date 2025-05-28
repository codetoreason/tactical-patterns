package dev.codetoreason.patterns.tactical.rule.example.professor.employment;

record FacultyCatalogEntry(
        FacultyId id,
        String name,
        FieldOfStudy mainFieldOfStudy,
        FieldsOfStudies secondaryFieldOfStudies
) {

    public int fieldsOfStudiesNumber() {
        return secondaryFieldOfStudies.count() + 1;
    }

    public FieldsOfStudies allFieldsOfStudies() {
        return secondaryFieldOfStudies.add(mainFieldOfStudy);
    }
}
