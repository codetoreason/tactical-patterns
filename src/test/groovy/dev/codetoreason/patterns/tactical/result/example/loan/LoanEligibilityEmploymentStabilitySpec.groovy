package dev.codetoreason.patterns.tactical.result.example.loan

import dev.codetoreason.patterns.tactical.money.Money

import static dev.codetoreason.patterns.tactical.money.Currency.PLN

class LoanEligibilityEmploymentStabilitySpec extends BaseLoanEligibilitySpec {

    def "should add 0 points for short employment and low income"() {
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

    def "should add 1 point for stable employment and low income"() {
        given:
            def applicant = validApplicantModifiedWith {
                employmentHistory(employmentHistoryModifiedWith {
                    monthsEmployed(30)
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

    def "should add 1 point for short employment and medium income"() {
        given:
            def applicant = validApplicantModifiedWith {
                employmentHistory(employmentHistoryModifiedWith {
                    averageIncome(new BigDecimal("9000"))
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

    def "should add 2 points for stable employment and medium income"() {
        given:
            def applicant = validApplicantModifiedWith {
                employmentHistory(employmentHistoryModifiedWith {
                    monthsEmployed(30)
                    averageIncome(new BigDecimal("9000"))
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

    def "should add 3 points for long employment and medium income"() {
        given:
            def applicant = validApplicantModifiedWith {
                employmentHistory(employmentHistoryModifiedWith {
                    monthsEmployed(65)
                    averageIncome(new BigDecimal("9000"))
                })
            }

        when:
            def result = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()
                                               .assessEligibility(APPLICANT_ID)

        then:
            result.get().approvedAmount() == Money.of(new BigDecimal("100000"), PLN)
    }

    def "should add 3 points for stable employment and high income"() {
        given:
            def applicant = validApplicantModifiedWith {
                employmentHistory(employmentHistoryModifiedWith {
                    monthsEmployed(30)
                    averageIncome(new BigDecimal("16000"))
                })
            }

        when:
            def result = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()
                                               .assessEligibility(APPLICANT_ID)

        then:
            result.get().approvedAmount() == Money.of(new BigDecimal("100000"), PLN)
    }

    def "should add 4 points for long employment and high income"() {
        given:
            def applicant = validApplicantModifiedWith {
                employmentHistory(employmentHistoryModifiedWith {
                    monthsEmployed(65)
                    averageIncome(new BigDecimal("16000"))
                })
            }

        when:
            def result = LoanEligibilityFixture.create()
                                               .withApplicant(applicant)
                                               .build()
                                               .assessEligibility(APPLICANT_ID)

        then:
            result.get().approvedAmount() == Money.of(new BigDecimal("200000"), PLN)
    }
}
