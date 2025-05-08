package dev.codetoreason.patterns.tactical.result.example.loan


import dev.codetoreason.patterns.tactical.money.Money

import static dev.codetoreason.patterns.tactical.money.Currency.PLN

class LoanEligibilityBadDebtProfileSpec extends BaseLoanEligibilitySpec {

    def "should reject when total monthly debt exceeds threshold"() {
        given:
            def applicant = validApplicantModifiedWith {
                debtProfile(debtProfileModifiedWith {
                    totalMonthlyDebt new BigDecimal("15000")
                })
            }
            def facade = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()

        when:
            def result = facade.assessEligibility(APPLICANT_ID)

        then:
            with(result.get()) {
                approvedAmount() == Money.zero(PLN)
            }
    }

    def "should reject when applicant has overdue installments"() {
        given:
            def applicant = validApplicantModifiedWith {
                debtProfile(debtProfileModifiedWith {
                    hasOverdueInstallments true
                })
            }
            def facade = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()

        when:
            def result = facade.assessEligibility(APPLICANT_ID)

        then:
            with(result.get()) {
                approvedAmount() == Money.zero(PLN)
            }
    }

    def "should reject when applicant has more than 5 active loans"() {
        given:
            def applicant = validApplicantModifiedWith {
                debtProfile(debtProfileModifiedWith {
                    numberOfActiveLoans 6
                })
            }
            def facade = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()

        when:
            def result = facade.assessEligibility(APPLICANT_ID)

        then:
            with(result.get()) {
                approvedAmount() == Money.zero(PLN)
            }
    }
}