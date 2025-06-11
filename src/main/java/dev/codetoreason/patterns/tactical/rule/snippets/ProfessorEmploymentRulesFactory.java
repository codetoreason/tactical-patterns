package dev.codetoreason.patterns.tactical.rule.snippets;

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

    private class YearsOfExperience implements RuleFactory<ProfessorEmploymentContext> {

        @Override
        public Rule<ProfessorEmploymentContext> create() {
            return Rule.when(this::hasEnoughExperience)
                       .orElse(this::failureMessage);
        }

        boolean hasEnoughExperience(ProfessorEmploymentContext context) {
            return context.application().yearsOfExperience() >= config.minYearsOfExperience();
        }

        private String failureMessage(ProfessorEmploymentContext context) {
            return context.application().professorName() + "has too small experience";
        }
    }

    private class FieldsOfStudy implements RuleFactory<ProfessorEmploymentContext> {

        @Override
        public Rule<ProfessorEmploymentContext> create() {
            return Rule.when(this::fieldsOfStudyMatched)
                       .orElse(this::failureMessage);
        }

        boolean fieldsOfStudyMatched(ProfessorEmploymentContext context) {
            var minMatchedFieldsOfStudy = config.minMatchedFieldsOfStudy();
            var professorFieldsOfStudy = context.application().fieldsOfStudies();
            var faculty = context.faculty();
            var facultyFieldsOfStudy = faculty.fieldsOfStudies();
            var facultyHasFewFieldsOfStudies = faculty.fieldsOfStudiesNumber() < minMatchedFieldsOfStudy;
            return facultyHasFewFieldsOfStudies
                    ? professorFieldsOfStudy.matchesAllOf(facultyFieldsOfStudy)
                    : professorFieldsOfStudy.numberOfMatched(facultyFieldsOfStudy) >= minMatchedFieldsOfStudy;
        }

        private String failureMessage(ProfessorEmploymentContext context) {
            return context.application().professorName() + "does not match faculty fields of studies";
        }
    }
}
