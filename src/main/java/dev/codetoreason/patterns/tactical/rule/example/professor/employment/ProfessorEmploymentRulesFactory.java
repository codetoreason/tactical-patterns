package dev.codetoreason.patterns.tactical.rule.example.professor.employment;

import dev.codetoreason.patterns.tactical.rule.Rule;
import dev.codetoreason.patterns.tactical.rule.RuleFactory;
import dev.codetoreason.patterns.tactical.rule.Rules;

class ProfessorEmploymentRulesFactory {

    private final ProfessorEmploymentConfig config;

    private ProfessorEmploymentRulesFactory(ProfessorEmploymentConfig config) {
        this.config = config;
    }

    static ProfessorEmploymentRulesFactory from(ProfessorEmploymentConfig config) {
        return new ProfessorEmploymentRulesFactory(config);
    }

    Rules<ProfessorEmploymentContext> createRules() {
        return Rules.when(new YearsOfExperience())
                    .and(new FieldsOfStudy())
                    .compose();
    }

    private abstract static class ProfessorEmploymentRuleFactory implements RuleFactory<ProfessorEmploymentContext> {

        @Override
        public Rule<ProfessorEmploymentContext> create() {
            return Rule.when(this::conditionIsMet)
                       .orElse(this::buildFailureMessage);
        }

        abstract boolean conditionIsMet(ProfessorEmploymentContext context);

        abstract String specificReason();

        String buildFailureMessage(ProfessorEmploymentContext context) {
            var application = context.application();
            return "Professor %s cannot be employed at faculty with id %s because %s"
                    .formatted(application.professorName(), application.facultyId(), specificReason());

        }
    }

    private class YearsOfExperience extends ProfessorEmploymentRuleFactory {

        @Override
        boolean conditionIsMet(ProfessorEmploymentContext context) {
            return context.application().yearsOfExperience() >= config.minYearsOfExperience();
        }

        @Override
        String specificReason() {
            return "has too small experience";
        }
    }

    private class FieldsOfStudy extends ProfessorEmploymentRuleFactory {

        @Override
        boolean conditionIsMet(ProfessorEmploymentContext context) {
            var minMatchedFieldsOfStudy = config.minMatchedFieldsOfStudy();
            var professorFieldsOfStudy = context.application().fieldsOfStudies();
            var faculty = context.faculty();
            var facultyFieldsOfStudy = faculty.allFieldsOfStudies();
            var facultyHasFewFieldsOfStudies = faculty.fieldsOfStudiesNumber() < minMatchedFieldsOfStudy;
            return facultyHasFewFieldsOfStudies
                    ? professorFieldsOfStudy.matchesAllOf(facultyFieldsOfStudy)
                    : professorFieldsOfStudy.numberOfMatched(facultyFieldsOfStudy) >= minMatchedFieldsOfStudy;
        }

        @Override
        String specificReason() {
            return "does not match faculty fields of studies";
        }
    }
}
