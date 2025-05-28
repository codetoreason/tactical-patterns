package dev.codetoreason.patterns.tactical.rule.example.professor.employment;

public record ProfessorEmploymentApplication(
        FacultyId facultyId,
        String professorName,
        int yearsOfExperience,
        FieldsOfStudies fieldsOfStudies
) {
}
