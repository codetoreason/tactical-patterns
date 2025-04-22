package dev.codetoreason.patterns.tactical.result.example.loan

class LoanEligibilityEmptyResultSpec extends BaseLoanEligibilitySpec {

    def "should return empty when applicant does not exist"() {
        given:
            def facade = LoanEligibilityFixture.withoutApplicants()

        when:
            def result = facade.assessEligibility(APPLICANT_ID)

        then:
            result.isEmpty()
    }
}
