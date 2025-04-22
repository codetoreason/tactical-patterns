package dev.codetoreason.patterns.tactical.result.example.loan;

import java.math.BigDecimal;

record EmploymentHistory(
        int monthsEmployed,
        boolean isCurrentlyEmployed,
        BigDecimal averageIncome
) {
}
