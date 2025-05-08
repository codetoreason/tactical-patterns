package dev.codetoreason.patterns.tactical.result.example.loan


import static dev.codetoreason.patterns.tactical.money.MoneyFactory.pln

class LoanEligibilityEmploymentStabilitySpec extends BaseLoanEligibilitySpec {

    static final def APPROVED_10K = pln("10000")
    static final def APPROVED_20K = pln("20000")
    static final def APPROVED_50K = pln("50000")
    static final def APPROVED_100K = pln("100000")
    static final def APPROVED_200K = pln("200000")

    def "should approve 10_000 PLN for short employment and low income"() {
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

    def "should approve 20_000 PLN for stable employment and low income"() {
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
            with(result.get()) {
                approvedAmount() == APPROVED_20K
            }
    }

    def "should approve 20_000 PLN for short employment and medium income"() {
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
            with(result.get()) {
                approvedAmount() == APPROVED_20K
            }
    }

    def "should approve 50_000 PLN for stable employment and medium income"() {
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
            with(result.get()) {
                approvedAmount() == APPROVED_50K
            }
    }

    def "should approve 100_000 PLN for long employment and medium income"() {
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
            with(result.get()) {
                approvedAmount() == APPROVED_100K
            }
    }

    def "should approve 100_000 PLN for stable employment and high income"() {
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
            with(result.get()) {
                approvedAmount() == APPROVED_100K
            }
    }

    def "should approve 200_000 PLN for long employment and high income"() {
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
            with(result.get()) {
                approvedAmount() == APPROVED_200K
            }
    }
}
