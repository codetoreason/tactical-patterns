package dev.codetoreason.patterns.tactical.result.example.loan


import spock.lang.Specification

import static dev.codetoreason.patterns.tactical.result.example.loan.ApplicantProfile.ApplicantProfileBuilder
import static dev.codetoreason.patterns.tactical.result.example.loan.CreditHistory.CreditHistoryBuilder
import static dev.codetoreason.patterns.tactical.result.example.loan.DebtProfile.DebtProfileBuilder
import static dev.codetoreason.patterns.tactical.result.example.loan.EmploymentHistory.EmploymentHistoryBuilder
import static groovy.lang.Closure.DELEGATE_FIRST

abstract class BaseLoanEligibilitySpec extends Specification {

    protected static final ApplicantId APPLICANT_ID = new ApplicantId("TEST_ID")

    protected static final ApplicantProfile VALID_APPLICANT = new ApplicantProfile(
            APPLICANT_ID,
            EmploymentHistory.builder()
                             .monthsEmployed(12)
                             .isCurrentlyEmployed(true)
                             .averageIncome(new BigDecimal("2000"))
                             .build(),
            CreditHistory.builder()
                         .missedPaymentsLast12Months(2)
                         .hasActiveCollectionCases(false)
                         .hasRecentBankruptcy(false)
                         .build(),
            DebtProfile.builder()
                       .totalMonthlyDebt(new BigDecimal("10000"))
                       .numberOfActiveLoans(5)
                       .hasOverdueInstallments(false)
                       .build()
    )

    protected static ApplicantProfile validApplicantModifiedWith(
            @DelegatesTo(value = ApplicantProfileBuilder, strategy = DELEGATE_FIRST) Closure<?> customizer
    ) {
        def builder = VALID_APPLICANT.toBuilder()
        customizer.delegate = builder
        customizer.resolveStrategy = DELEGATE_FIRST
        customizer.call()
        builder.build()
    }

    protected static EmploymentHistory employmentHistoryModifiedWith(
            @DelegatesTo(value = EmploymentHistoryBuilder, strategy = DELEGATE_FIRST) Closure<?> modifier
    ) {
        def builder = VALID_APPLICANT.employmentHistory().toBuilder()
        modifier.delegate = builder
        modifier.resolveStrategy = DELEGATE_FIRST
        modifier.call()
        builder.build()
    }

    protected static CreditHistory creditHistoryModifiedWith(
            @DelegatesTo(value = CreditHistoryBuilder, strategy = DELEGATE_FIRST) Closure<?> modifier
    ) {
        def builder = VALID_APPLICANT.creditHistory().toBuilder()
        modifier.delegate = builder
        modifier.resolveStrategy = DELEGATE_FIRST
        modifier.call()
        builder.build()
    }

    protected static DebtProfile debtProfileModifiedWith(
            @DelegatesTo(value = DebtProfileBuilder, strategy = DELEGATE_FIRST) Closure<?> modifier
    ) {
        def builder = VALID_APPLICANT.debtProfile().toBuilder()
        modifier.delegate = builder
        modifier.resolveStrategy = DELEGATE_FIRST
        modifier.call()
        builder.build()
    }
}