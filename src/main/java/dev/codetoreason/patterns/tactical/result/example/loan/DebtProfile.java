package dev.codetoreason.patterns.tactical.result.example.loan;

import java.math.BigDecimal;

record DebtProfile(
        BigDecimal totalMonthlyDebt,
        int numberOfActiveLoans,
        boolean hasOverdueInstallments
) {}
