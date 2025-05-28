package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment;

import lombok.Builder;

@Builder
record ProfessorEmploymentContext(
        ProfessorEmploymentApplication application,
        FacultyCatalogEntry faculty
) {
}
