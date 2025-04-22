package dev.codetoreason.patterns.tactical.result.example.loan;

import dev.codetoreason.patterns.tactical.result.OperationResult;

import java.math.BigDecimal;

class DebtBurdenValidator implements ApplicantValidator {

    private static final BigDecimal MAX_TOTAL_DEBT = new BigDecimal("10000"); // np. 10 000 PLN
    private static final int MAX_NUMBER_OF_ACTIVE_LOANS = 5;

    public OperationResult validate(ApplicantProfile profile) {
        var debt = profile.monthlyDebt();
        if (debt.totalMonthlyDebt().compareTo(MAX_TOTAL_DEBT) > 0) {
            return OperationResult.failed("Total monthly debt exceeds safe threshold");
        }

        if (debt.hasOverdueInstallments()) {
            return OperationResult.failed("Applicant has overdue loan installments");
        }

        if (debt.numberOfActiveLoans() > MAX_NUMBER_OF_ACTIVE_LOANS) {
            return OperationResult.failed("Too many active loans");
        }

        return OperationResult.successful();
    }
}

