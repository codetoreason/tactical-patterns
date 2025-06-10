package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment

import spock.lang.Specification

import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentFixture.IT_FACULTY_ID
import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentFixture.ZUCK
import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentFixture.applicationWith

class ProfessorEmploymentFlowSpec extends Specification {

    def "should return empty when rules fail"() {
        given:
            def fixture = ProfessorEmploymentFixture.create()
                                                    .withRulesExaminedNegatively()
                                                    .withFacultyContainingFieldsOfStudies("cs", "ai", "math")
            def facade = fixture.buildFacade()

        when:
            def result = facade.employProfessor(
                    applicationWith {
                        yearsOfExperience(1)
                        fieldsOfStudies("cs", "ai", "math")
                    }
            )

        then:
            result.isEmpty()
            fixture.noProfessorPersisted()
    }

    def "should return empty when employment operation fails"() {
        given:
            def fixture = ProfessorEmploymentFixture.create()
                                                    .withRulesExaminedSuccessfully()
                                                    .withFailedEmploymentOperation()
                                                    .withFacultyContainingFieldsOfStudies("cs", "ai", "math")
            def facade = fixture.buildFacade()

        when:
            def result = facade.employProfessor(
                    applicationWith {
                        yearsOfExperience(6)
                        fieldsOfStudies("cs", "ai", "math")
                    }
            )

        then:
            result.isEmpty()
            fixture.noProfessorPersisted()
    }

    def "should throw when faculty not found"() {
        given:
            def fixture = ProfessorEmploymentFixture.create()
                                                    .withRulesExaminedSuccessfully()
            def facade = fixture.buildFacade()

        when:
            facade.employProfessor(
                    applicationWith {
                        yearsOfExperience(6)
                        fieldsOfStudies("cs", "ai")
                    }
            )

        then:
            def ex = thrown(NoSuchElementException)
            ex.message.contains("Entity with ID $IT_FACULTY_ID not found")
    }

    def "should employ professor when all rules pass"() {
        given:
            def fixture = ProfessorEmploymentFixture.create()
                                                    .withRulesExaminedSuccessfully()
                                                    .withSuccessfulEmploymentOperation()
                                                    .withFacultyContainingFieldsOfStudies("cs", "ai", "math")
            def facade = fixture.buildFacade()

        when:
            def result = facade.employProfessor(
                    applicationWith {
                        yearsOfExperience(10)
                        fieldsOfStudies("cs", "ai", "math")
                    }
            )

        then:
            result.isPresent()
            with(fixture.getProfessorById(result.get())) {
                name() == ZUCK
                fieldsOfStudies() == FieldsOfStudies.of("cs", "ai", "math")
            }
    }
}
