package dev.codetoreason.patterns.tactical.result.example.loan

import dev.codetoreason.patterns.tactical.money.Money

import static dev.codetoreason.patterns.tactical.money.Currency.PLN

class LoanEligibilityCreditHistorySpec extends BaseLoanEligibilitySpec {

    def "should add 0 points when applicant missed more than 1 payment"() {
        given:
            def applicant = VALID_APPLICANT

        when:
            def result = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()
                                               .assessEligibility(APPLICANT_ID)

        then:
            result.get().approvedAmount() == Money.of(new BigDecimal("10000"), PLN)
    }

    def "should add 1 point when applicant missed exactly 1 payment but no collections or bankruptcy"() {
        given:
            def applicant = validApplicantModifiedWith {
                creditHistory(creditHistoryModifiedWith {
                    missedPaymentsLast12Months(1)
                    hasActiveCollectionCases(false)
                    hasRecentBankruptcy(false)
                })
            }

        when:
            def result = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()
                                               .assessEligibility(APPLICANT_ID)

        then:
            result.get().approvedAmount() == Money.of(new BigDecimal("20000"), PLN)
    }

    def "should add 2 points for perfect credit history (no missed payments, no issues)"() {
        given:
            def applicant = validApplicantModifiedWith {
                creditHistory(creditHistoryModifiedWith {
                    missedPaymentsLast12Months(0)
                    hasActiveCollectionCases(false)
                    hasRecentBankruptcy(false)
                })
            }

        when:
            def result = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()
                                               .assessEligibility(APPLICANT_ID)

        then:
            result.get().approvedAmount() == Money.of(new BigDecimal("50000"), PLN)
    }
}
