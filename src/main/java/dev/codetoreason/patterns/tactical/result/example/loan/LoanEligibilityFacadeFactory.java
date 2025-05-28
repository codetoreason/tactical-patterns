package dev.codetoreason.patterns.tactical.result.example.loan;

import java.util.List;

public class LoanEligibilityFacadeFactory {

    LoanEligibilityFacade create(ApplicantProfileRepository applicantRepository) {
        return new LoanEligibilityFacade(
                applicantRepository,
                new LoanEligibilityAssessor(
                        List.of(
                                new EmploymentStabilityValidator(),
                                new CreditHistoryValidator(),
                                new DebtBurdenValidator()
                        ),
                        new CreditLimitCalculator())
        );
    }
}
