package dev.codetoreason.patterns.tactical.result.example.loan;

import dev.codetoreason.patterns.tactical.result.OperationResult;

class CreditHistoryValidator implements ApplicantValidator {

    private static final int MAX_ALLOWED_MISSED_PAYMENTS = 2;

    @Override
    public OperationResult validate(ApplicantProfile profile) {
        var history = profile.creditHistory();
        if (history.hasRecentBankruptcy()) {
            return OperationResult.failed("Applicant declared bankruptcy recently");
        }
        if (history.hasActiveCollectionCases()) {
            return OperationResult.failed("Applicant has active debt collection cases");
        }
        if (history.missedPaymentsLast12Months() > MAX_ALLOWED_MISSED_PAYMENTS) {
            return OperationResult.failed("More than 2 missed payments in the last 12 months");
        }
        return OperationResult.successful();
    }
}

