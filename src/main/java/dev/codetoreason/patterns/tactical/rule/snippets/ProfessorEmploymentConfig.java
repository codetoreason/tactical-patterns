package dev.codetoreason.patterns.tactical.rule.snippets;

import lombok.Builder;

@Builder
record ProfessorEmploymentConfig(
        int minYearsOfExperience,
        int minMatchedFieldsOfStudy,
        int maxCourseLeaderships
) {
}
