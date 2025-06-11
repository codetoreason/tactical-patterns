package dev.codetoreason.patterns.tactical.rule.snippets;

import lombok.Builder;

@Builder
record ProfessorEmploymentContext(
        ProfessorEmploymentApplication application,
        FacultyCatalogEntry faculty
) {
}
