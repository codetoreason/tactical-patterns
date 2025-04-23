package dev.codetoreason.patterns.tactical.result.example.loan;

import dev.codetoreason.patterns.tactical.money.Money;

import java.math.BigDecimal;
import java.util.Map;

import static dev.codetoreason.patterns.tactical.money.Currency.PLN;

class CreditLimitCalculator {

    private static final int LONG_TERM_EMPLOYMENT = 60;
    private static final int STABLE_EMPLOYMENT = 24;

    private static final BigDecimal HIGH_INCOME = new BigDecimal("15000");
    private static final BigDecimal MEDIUM_INCOME = new BigDecimal("8000");

    private static final BigDecimal LOW_DTI_THRESHOLD = new BigDecimal("0.2");
    private static final BigDecimal MEDIUM_DTI_THRESHOLD = new BigDecimal("0.35");

    private static final BigDecimal DEFAULT_LIMIT = new BigDecimal("10000");

    private static final Map<Integer, BigDecimal> SCORE_TO_CREDIT_LIMIT = Map.of(
            8, new BigDecimal("5000000"),
            7, new BigDecimal("2000000"),
            6, new BigDecimal("1000000"),
            5, new BigDecimal("500000"),
            4, new BigDecimal("200000"),
            3, new BigDecimal("100000"),
            2, new BigDecimal("50000"),
            1, new BigDecimal("20000")
    );

    Money suggestLimit(CreditAssessmentData creditAssessmentData) {
        var score = 0;
        score += scoreEmploymentStability(creditAssessmentData);
        score += scoreCreditHistory(creditAssessmentData);
        score += scoreDebtBurden(creditAssessmentData);

        var limit = SCORE_TO_CREDIT_LIMIT.getOrDefault(score, DEFAULT_LIMIT);
        return Money.of(limit, PLN);
    }

    private int scoreEmploymentStability(CreditAssessmentData creditAssessmentData) {
        var points = 0;

        var monthsEmployed = creditAssessmentData.monthsEmployed();
        if (monthsEmployed >= LONG_TERM_EMPLOYMENT) {
            points += 2;
        } else if (monthsEmployed >= STABLE_EMPLOYMENT) {
            points += 1;
        }

        var income = creditAssessmentData.averageIncome();
        if (income.compareTo(HIGH_INCOME) >= 0) {
            points += 2;
        } else if (income.compareTo(MEDIUM_INCOME) >= 0) {
            points += 1;
        }

        return points;
    }

    private int scoreCreditHistory(CreditAssessmentData creditAssessmentData) {
        var missedPaymentsLast12Months = creditAssessmentData.missedPaymentsLast12Months();
        if (missedPaymentsLast12Months == 0) {
            return 2;
        }
        if (missedPaymentsLast12Months <= 1) {
            return 1;
        }
        return 0;
    }

    private int scoreDebtBurden(CreditAssessmentData creditAssessmentData) {
        var dti = creditAssessmentData.calculateDti();

        if (dti.compareTo(LOW_DTI_THRESHOLD) <= 0
                && creditAssessmentData.numberOfActiveLoans() <= 2) {
            return 2;
        }

        if (dti.compareTo(MEDIUM_DTI_THRESHOLD) <= 0) {
            return 1;
        }

        return 0;
    }
}
