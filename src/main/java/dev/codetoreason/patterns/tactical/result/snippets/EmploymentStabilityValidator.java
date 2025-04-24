package dev.codetoreason.patterns.tactical.result.snippets;

import dev.codetoreason.patterns.tactical.result.OperationResult;

import java.math.BigDecimal;

class EmploymentStabilityValidator implements ApplicantValidator {

    private static final int MINIMUM_MONTHS_EMPLOYED = 12;
    private static final BigDecimal MINIMUM_AVERAGE_INCOME = BigDecimal.valueOf(2000);

    @Override
    public OperationResult validate(ApplicantProfile profile) {
        var history = profile.employmentHistory();
        if (history.monthsEmployed() < MINIMUM_MONTHS_EMPLOYED) {
            return OperationResult.failed("Less than 12 months of work history");
        }
        if (!history.isCurrentlyEmployed()) {
            return OperationResult.failed("Applicant is not currently employed");
        }
        if (history.averageIncome().compareTo(MINIMUM_AVERAGE_INCOME) < 0) {
            return OperationResult.failed("Average income below minimum threshold");
        }
        return OperationResult.successful();
    }
}
