package dev.codetoreason.patterns.tactical.result.example.loan;

import lombok.Builder;

@Builder(toBuilder = true)
record ApplicantProfile(
        ApplicantId id,
        EmploymentHistory employmentHistory,
        CreditHistory creditHistory,
        DebtProfile debtProfile
) {}

