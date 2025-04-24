package dev.codetoreason.patterns.tactical.result.snippets

import spock.lang.Specification
import spock.lang.Subject

import static groovy.lang.Closure.DELEGATE_FIRST

class EmploymentStabilityValidatorSpec extends Specification {

    static final def APPLICANT_ID = new ApplicantId("TEST_ID")
    static final def VALID_APPLICANT = new ApplicantProfile(
            APPLICANT_ID,
            EmploymentHistory.builder()
                             .monthsEmployed(12)
                             .isCurrentlyEmployed(true)
                             .averageIncome(new BigDecimal("2000"))
                             .build()
    )

    @Subject
    def validator = new EmploymentStabilityValidator()

    def "should return success for valid employment profile"() {
        given:
            def profile = VALID_APPLICANT

        when:
            def result = validator.validate(profile)

        then:
            result.success
    }

    def "should fail if monthsEmployed is less than 12"() {
        given:
            def profile = validApplicantModifiedWith {
                monthsEmployed(6)
            }

        when:
            def result = validator.validate(profile)

        then:
            result.failure
            result.message() == "Less than 12 months of work history"
    }

    def "should fail if applicant is not currently employed"() {
        given:
            def profile = validApplicantModifiedWith {
                isCurrentlyEmployed(false)
            }

        when:
            def result = validator.validate(profile)

        then:
            result.failure
            result.message() == "Applicant is not currently employed"
    }

    def "should fail if income is below minimum 2000"() {
        given:
            def profile = validApplicantModifiedWith {
                averageIncome(new BigDecimal("1500"))
            }

        when:
            def result = validator.validate(profile)

        then:
            result.failure
            result.message() == "Average income below minimum threshold"
    }

    protected static ApplicantProfile validApplicantModifiedWith(
            @DelegatesTo(value = EmploymentHistory.EmploymentHistoryBuilder, strategy = DELEGATE_FIRST) Closure<?> modifier
    ) {
        def builder = VALID_APPLICANT.employmentHistory().toBuilder()
        modifier.delegate = builder
        modifier.resolveStrategy = DELEGATE_FIRST
        modifier.call()
        return new ApplicantProfile(APPLICANT_ID, builder.build())
    }
}

