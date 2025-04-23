package dev.codetoreason.patterns.tactical.result.example.loan;

import lombok.Builder;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

@Builder
record CreditAssessmentData(
        int monthsEmployed,
        BigDecimal averageIncome,
        int missedPaymentsLast12Months,
        BigDecimal totalMonthlyDebt,
        int numberOfActiveLoans
) {

    BigDecimal calculateDti() {
        return averageIncome.compareTo(ZERO) > 0
                ? totalMonthlyDebt.divide(averageIncome, 4, HALF_UP)
                : ONE;
    }
}
