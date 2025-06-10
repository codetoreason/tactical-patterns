package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment


import spock.lang.Specification
import spock.lang.Subject

import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentFixture.DEFAULT_CONFIG
import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentFixture.IT_FACULTY_NAME
import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentFixture.ZUCK
import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentFixture.applicationWith
import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentFixture.contextWith
import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentFixture.facultyWith

class ProfessorEmploymentValidationSpec extends Specification {

    @Subject
    static final def RULES = ProfessorEmploymentRulesFactory.from(DEFAULT_CONFIG)
                                                            .createRules()

    def "should fail when years of experience are below threshold"() {
        given:
            def context = contextWith {
                application(applicationWith {
                    yearsOfExperience(4)
                    fieldsOfStudies("math", "physics", "cs")
                })
                faculty(facultyWith {
                    fieldsOfStudies("math", "physics", "cs")
                })
            }

        when:
            def result = RULES.examine(context)

        then:
            result.isFailure()
            with(result.message()) {
                contains(ZUCK)
                contains(IT_FACULTY_NAME)
                contains("has too small experience")
            }
    }

    def "should fail when fields of study don’t match enough"() {
        given:
            def context = contextWith {
                application(applicationWith {
                    yearsOfExperience(10)
                    fieldsOfStudies("math", "physics")
                })
                faculty(facultyWith {
                    fieldsOfStudies("math", "physics", "cs")
                })
            }

        when:
            def result = RULES.examine(context)

        then:
            result.isFailure()
            with(result.message()) {
                contains(ZUCK)
                contains(IT_FACULTY_NAME)
                contains("does not match faculty fields of studies")
            }
    }

    def "should fail when faculty has few fields and candidate doesn’t match all"() {
        given:
            def context = contextWith {
                application(applicationWith {
                    yearsOfExperience(5)
                    fieldsOfStudies("biology")
                })
                faculty(facultyWith {
                    fieldsOfStudies("cs")
                })
            }

        when:
            def result = RULES.examine(context)

        then:
            result.isFailure()
            with(result.message()) {
                contains(ZUCK)
                contains(IT_FACULTY_NAME)
                contains("does not match faculty fields of studies")
            }
    }

    def "should fail when faculty has few fields and candidate does not include all of them"() {
        given:
            def context = contextWith {
                application(applicationWith {
                    yearsOfExperience(5)
                    fieldsOfStudies("math", "biology")
                })
                faculty(facultyWith {
                    fieldsOfStudies("math", "cs")
                })
            }

        when:
            def result = RULES.examine(context)

        then:
            result.isFailure()
            with(result.message()) {
                contains(ZUCK)
                contains(IT_FACULTY_NAME)
                contains("does not match faculty fields of studies")
            }
    }

    def "should pass when experience and fields match"() {
        given:
            def context = contextWith {
                application(applicationWith {
                    yearsOfExperience(6)
                    fieldsOfStudies("math", "physics")
                })
                faculty(facultyWith {
                    fieldsOfStudies("math", "physics")
                })
            }

        expect:
            RULES.examine(context).isSuccess()
    }

    def "should pass when faculty has few fields but candidate matches all"() {
        given:
            def context = contextWith {
                application(applicationWith {
                    yearsOfExperience(5)
                    fieldsOfStudies("cs")
                })
                faculty(facultyWith {
                    fieldsOfStudies("cs")
                })
            }

        expect:
            RULES.examine(context).isSuccess()
    }

    def "should pass when candidate matches enough fields for rich faculty"() {
        given:
            def context = contextWith {
                application(applicationWith {
                    yearsOfExperience(6)
                    fieldsOfStudies("math", "physics", "cs")
                })
                faculty(facultyWith {
                    fieldsOfStudies("math", "physics", "cs", "english")
                })
            }

        expect:
            RULES.examine(context).isSuccess()
    }
}
