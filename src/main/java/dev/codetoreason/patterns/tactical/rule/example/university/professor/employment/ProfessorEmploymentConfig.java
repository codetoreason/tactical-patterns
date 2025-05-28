package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment;

import lombok.Builder;

@Builder
record ProfessorEmploymentConfig(
        int minYearsOfExperience,
        int minMatchedFieldsOfStudy,
        int maxCourseLeaderships
) {
}
