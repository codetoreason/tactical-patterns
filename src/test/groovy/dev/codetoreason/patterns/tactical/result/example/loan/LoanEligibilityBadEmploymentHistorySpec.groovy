package dev.codetoreason.patterns.tactical.result.example.loan

import dev.codetoreason.patterns.tactical.money.Money

import static dev.codetoreason.patterns.tactical.money.Currency.PLN

class LoanEligibilityBadEmploymentHistorySpec extends BaseLoanEligibilitySpec {

    def "should reject when months employed is below threshold"() {
        given:
            def applicant = validApplicantModifiedWith {
                employmentHistory(employmentHistoryModifiedWith {
                    monthsEmployed 6
                })
            }
            def facade = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()

        when:
            def result = facade.assessEligibility(APPLICANT_ID)

        then:
            result.isPresent()
            with(result.get()) {
                approvedAmount() == Money.zero(PLN)
            }
    }

    def "should reject when applicant is not currently employed"() {
        given:
            def applicant = validApplicantModifiedWith {
                employmentHistory(employmentHistoryModifiedWith {
                    isCurrentlyEmployed false
                })
            }
            def facade = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()

        when:
            def result = facade.assessEligibility(APPLICANT_ID)

        then:
            result.isPresent()
            with(result.get()) {
                approvedAmount() == Money.zero(PLN)
            }
    }

    def "should reject when applicant has income below threshold"() {
        given:
            def applicant = validApplicantModifiedWith {
                employmentHistory(employmentHistoryModifiedWith {
                    averageIncome new BigDecimal("1500")
                })
            }
            def facade = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()

        when:
            def result = facade.assessEligibility(APPLICANT_ID)

        then:
            result.isPresent()
            with(result.get()) {
                approvedAmount() == Money.zero(PLN)
            }
    }
}
