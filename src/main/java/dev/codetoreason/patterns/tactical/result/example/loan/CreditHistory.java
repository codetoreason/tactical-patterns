package dev.codetoreason.patterns.tactical.result.example.loan;

record CreditHistory(
        int missedPaymentsLast12Months,
        boolean hasActiveCollectionCases,
        boolean hasRecentBankruptcy
) {}
