package dev.codetoreason.patterns.tactical.rule.snippets;

import lombok.Builder;

@Builder
record ProfessorEmploymentApplication(
        FacultyId facultyId,
        String professorName,
        int yearsOfExperience,
        FieldsOfStudies fieldsOfStudies
) {
}
