package dev.codetoreason.patterns.tactical.result.example.loan;

import dev.codetoreason.patterns.tactical.infra.repository.Entity;
import lombok.Builder;

@Builder(toBuilder = true)
record ApplicantProfile(
        ApplicantId id,
        EmploymentHistory employmentHistory,
        CreditHistory creditHistory,
        DebtProfile debtProfile
) implements Entity<ApplicantId> {

    CreditAssessmentData toCreditAssessmentData() {
        return CreditAssessmentData.builder()
                                   .monthsEmployed(employmentHistory.monthsEmployed())
                                   .averageIncome(employmentHistory.averageIncome())
                                   .missedPaymentsLast12Months(creditHistory.missedPaymentsLast12Months())
                                   .totalMonthlyDebt(debtProfile.totalMonthlyDebt())
                                   .numberOfActiveLoans(debtProfile.numberOfActiveLoans())
                                   .build();
    }
}

