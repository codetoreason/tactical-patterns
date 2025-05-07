package dev.codetoreason.patterns.tactical.result.example.loan

import dev.codetoreason.patterns.tactical.money.Money
import spock.lang.Unroll

import static dev.codetoreason.patterns.tactical.money.Currency.PLN

class LoanEligibilityDebtBurdenSpec extends BaseLoanEligibilitySpec {

    def "should add 0 points when DTI > 0.35 regardless of active loans"() {
        given:
            def applicant = VALID_APPLICANT

        when:
            def result = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()
                                               .assessEligibility(APPLICANT_ID)

        then:
            result.isPresent()
            with(result.get()) {
                approvedAmount() == Money.of(new BigDecimal("10000"), PLN)
            }
        and:
            applicant.toCreditAssessmentData().calculateDti() > new BigDecimal("0.35")
    }

    def "should add 1 point when 0.2 < DTI <= 0.35 regardless of active loans"() {
        given:
            def applicant = validApplicantModifiedWith {
                debtProfile(debtProfileModifiedWith {
                    totalMonthlyDebt(new BigDecimal("700"))
                    numberOfActiveLoans(1)
                })
            }

        when:
            def result = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()
                                               .assessEligibility(APPLICANT_ID)

        then:
            result.isPresent()
            with(result.get()) {
                approvedAmount() == Money.of(new BigDecimal("20000"), PLN)
            }
        and:
            applicant.toCreditAssessmentData().calculateDti() <= new BigDecimal("0.35")
    }

    @Unroll("#dataVariables")
    def "should add 1 point when DTI <= 0.2 but active loans > 2"() {
        given:
            def applicant = validApplicantModifiedWith {
                debtProfile(debtProfileModifiedWith {
                    totalMonthlyDebt(new BigDecimal(monthlyDebtValue))
                    numberOfActiveLoans(3)
                })
            }

        when:
            def result = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()
                                               .assessEligibility(APPLICANT_ID)

        then:
            result.isPresent()
            with(result.get()) {
                approvedAmount() == Money.of(new BigDecimal("20000"), PLN)
            }
        and:
            applicant.toCreditAssessmentData().calculateDti() == new BigDecimal(dtiValue)

        where:
            monthlyDebtValue | dtiValue
            "400"            | "0.2"
            "300"            | "0.15"
            "400"            | "0.2"
            "300"            | "0.15"
    }

    @Unroll("#dataVariables")
    def "should add 2 points when DTI <= 0.2 and active loans <= 2"() {
        given:
            def applicant = validApplicantModifiedWith {
                debtProfile(debtProfileModifiedWith {
                    totalMonthlyDebt(new BigDecimal(monthlyDebtValue))
                    numberOfActiveLoans(activeLoans)
                })
            }

        when:
            def result = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()
                                               .assessEligibility(APPLICANT_ID)

        then:
            result.isPresent()
            with(result.get()) {
                approvedAmount() == Money.of(new BigDecimal("50000"), PLN)
            }
        and:
            applicant.toCreditAssessmentData().calculateDti() == new BigDecimal(dtiValue)

        where:
            monthlyDebtValue | dtiValue | activeLoans
            "400"            | "0.2"    | 2
            "300"            | "0.15"   | 2
            "400"            | "0.2"    | 1
            "300"            | "0.15"   | 1
    }
}
