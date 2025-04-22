package dev.codetoreason.patterns.tactical.result.example.loan;

import lombok.Builder;

@Builder(toBuilder = true)
record CreditHistory(
        int missedPaymentsLast12Months,
        boolean hasActiveCollectionCases,
        boolean hasRecentBankruptcy
) {}
