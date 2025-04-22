package dev.codetoreason.patterns.tactical.result.example.loan;

import lombok.Builder;

import java.math.BigDecimal;

@Builder(toBuilder = true)
record EmploymentHistory(
        int monthsEmployed,
        boolean isCurrentlyEmployed,
        BigDecimal averageIncome
) {
}
