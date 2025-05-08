package dev.codetoreason.patterns.tactical.result.example.loan


import static dev.codetoreason.patterns.tactical.money.MoneyFactory.pln

class LoanEligibilityCreditHistorySpec extends BaseLoanEligibilitySpec {

    static final def APPROVED_10K = pln("10000")
    static final def APPROVED_20K = pln("20000")
    static final def APPROVED_50K = pln("50000")

    def "should approve 10_000 PLN when applicant missed more than 1 payment"() {
        given:
            def applicant = VALID_APPLICANT

        when:
            def result = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()
                                               .assessEligibility(APPLICANT_ID)

        then:
            with(result.get()) {
                approvedAmount() == APPROVED_10K
            }
    }

    def "should approve 20_000 PLN when applicant missed exactly 1 payment but no collections or bankruptcy"() {
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
            with(result.get()) {
                approvedAmount() == APPROVED_20K
            }
    }

    def "should approve 50_000 PLN for perfect credit history - no missed payments, no issues"() {
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
            with(result.get()) {
                approvedAmount() == APPROVED_50K
            }
    }
}
