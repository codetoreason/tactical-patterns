package dev.codetoreason.patterns.tactical.result.example.loan;

import dev.codetoreason.patterns.tactical.money.Money;

public record LoanEligibilityResult(
        ApplicantId applicantId,
        Money approvedAmount
) {}

