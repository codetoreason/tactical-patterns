package dev.codetoreason.patterns.tactical.result.example.loan

import dev.codetoreason.patterns.tactical.money.Money
import spock.lang.Unroll

import static dev.codetoreason.patterns.tactical.money.Currency.PLN
import static dev.codetoreason.patterns.tactical.money.MoneyFactory.pln
import static dev.codetoreason.patterns.tactical.money.MoneyFactory.pln
import static dev.codetoreason.patterns.tactical.money.MoneyFactory.pln

class LoanEligibilityDebtBurdenSpec extends BaseLoanEligibilitySpec {

    static final def APPROVED_10K = pln("10000")
    static final def APPROVED_20K = pln("20000")
    static final def APPROVED_50K = pln("50000")

    def "should approve 10_000 PLN when DTI > 0.35 regardless of active loans"() {
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
        and:
            applicant.toCreditAssessmentData().calculateDti() > new BigDecimal("0.35")
    }

    def "should approve 20_000 PLN when 0.2 < DTI <= 0.35 regardless of active loans"() {
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
            with(result.get()) {
                approvedAmount() == APPROVED_20K
            }
        and:
            applicant.toCreditAssessmentData().calculateDti() <= new BigDecimal("0.35")
    }

    @Unroll("#dataVariables")
    def "should approve 20_000 PLN when DTI <= 0.2 but active loans > 2"() {
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
            with(result.get()) {
                approvedAmount() == APPROVED_20K
            }
        and:
            applicant.toCreditAssessmentData().calculateDti() == new BigDecimal(dtiValue)

        where:
            monthlyDebtValue | dtiValue
            "400"            | "0.2"
            "300"            | "0.15"
    }

    @Unroll("#dataVariables")
    def "should approve 50_000 PLN when DTI <= 0.2 and active loans <= 2"() {
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
            with(result.get()) {
                approvedAmount() == APPROVED_50K
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
