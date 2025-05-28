package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment;

import lombok.Builder;

@Builder
public record ProfessorEmploymentApplication(
        FacultyId facultyId,
        String professorName,
        int yearsOfExperience,
        FieldsOfStudies fieldsOfStudies
) {
}
