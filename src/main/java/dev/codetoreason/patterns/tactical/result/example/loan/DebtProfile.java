package dev.codetoreason.patterns.tactical.result.example.loan;

import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
record DebtProfile(
        BigDecimal totalMonthlyDebt,
        int numberOfActiveLoans,
        boolean hasOverdueInstallments
) {}
