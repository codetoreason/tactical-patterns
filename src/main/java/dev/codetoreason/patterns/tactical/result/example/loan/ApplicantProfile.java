package dev.codetoreason.patterns.tactical.result.example.loan;

import lombok.Builder;

@Builder(toBuilder = true)
record ApplicantProfile(
        ApplicantId id,
        EmploymentHistory employmentHistory,
        CreditHistory creditHistory,
        DebtProfile debtProfile
) {

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

