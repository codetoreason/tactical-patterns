package dev.codetoreason.patterns.tactical.result.example.loan;

import dev.codetoreason.patterns.tactical.money.Money;

import java.math.BigDecimal;
import java.util.Map;

import static dev.codetoreason.patterns.tactical.money.Currency.PLN;
import static java.math.RoundingMode.HALF_UP;

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
            2, new BigDecimal("50000")
    );

    Money suggestLimit(ApplicantProfile profile) {
        var score = 0;
        score += scoreEmploymentStability(profile);
        score += scoreCreditHistory(profile);
        score += scoreDebtBurden(profile);

        var limit = SCORE_TO_CREDIT_LIMIT.getOrDefault(score, DEFAULT_LIMIT);
        return Money.of(limit, PLN);
    }

    private int scoreEmploymentStability(ApplicantProfile profile) {
        var points = 0;

        var monthsEmployed = profile.employmentHistory().monthsEmployed();
        if (monthsEmployed >= LONG_TERM_EMPLOYMENT) {
            points += 2;
        } else if (monthsEmployed >= STABLE_EMPLOYMENT) {
            points += 1;
        }

        var income = profile.employmentHistory().averageIncome();
        if (income.compareTo(HIGH_INCOME) >= 0) {
            points += 2;
        } else if (income.compareTo(MEDIUM_INCOME) >= 0) {
            points += 1;
        }

        return points;
    }

    private int scoreCreditHistory(ApplicantProfile profile) {
        var history = profile.creditHistory();
        if (!history.hasRecentBankruptcy()
                && !history.hasActiveCollectionCases()
                && history.missedPaymentsLast12Months() == 0) {
            return 2;
        }

        if (history.missedPaymentsLast12Months() <= 1) {
            return 1;
        }

        return 0;
    }

    private int scoreDebtBurden(ApplicantProfile profile) {
        var debt = profile.monthlyDebt();
        var income = profile.employmentHistory().averageIncome();
        var dti = income.compareTo(BigDecimal.ZERO) > 0
                ? debt.totalMonthlyDebt().divide(income, 4, HALF_UP)
                : BigDecimal.ONE;

        if (dti.compareTo(LOW_DTI_THRESHOLD) <= 0
                && !debt.hasOverdueInstallments()
                && debt.numberOfActiveLoans() <= 2) {
            return 2;
        }

        if (dti.compareTo(MEDIUM_DTI_THRESHOLD) <= 0) {
            return 1;
        }

        return 0;
    }
}
