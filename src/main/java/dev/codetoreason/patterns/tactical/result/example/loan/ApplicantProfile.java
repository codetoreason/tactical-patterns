package dev.codetoreason.patterns.tactical.result.example.loan;

record ApplicantProfile(
        ApplicantId id,
        EmploymentHistory employmentHistory,
        CreditHistory creditHistory,
        DebtProfile debtProfile
) {}

