package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProfessorEmploymentFactory {

    ProfessorEmploymentConfig professorEmploymentConfig;
    FacultyCatalog facultyCatalog;
    ProfessorCatalog professorCatalog;
    ProfessorEmployment professorEmployment;

    public ProfessorEmploymentFacade professorEmploymentFacade() {
        return new ProfessorEmploymentFacade(
                professorEmploymentConfig,
                ProfessorEmploymentRulesFactory.from(professorEmploymentConfig)
                                               .createRules(),
                facultyCatalog,
                professorCatalog,
                professorEmployment
        );
    }
}
