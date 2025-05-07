package dev.codetoreason.patterns.tactical.result.example.loan

import dev.codetoreason.patterns.tactical.money.Money

import static dev.codetoreason.patterns.tactical.money.Currency.PLN

class LoanEligibilityBadCreditHistorySpec extends BaseLoanEligibilitySpec {

    def "should reject when applicant has recent bankruptcy"() {
        given:
            def applicant = validApplicantModifiedWith {
                creditHistory(creditHistoryModifiedWith {
                    hasRecentBankruptcy true
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


    def "should reject when applicant has active collection cases"() {
        given:
            def applicant = validApplicantModifiedWith {
                creditHistory(creditHistoryModifiedWith {
                    hasActiveCollectionCases true
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

    def "should reject when applicant missed more than 2 payments"() {
        given:
            def applicant = validApplicantModifiedWith {
                creditHistory(creditHistoryModifiedWith {
                    missedPaymentsLast12Months 3
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
