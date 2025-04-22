package dev.codetoreason.patterns.tactical.result.example.loan;

import java.util.List;

public class LoanEligibilityFacadeConfig {

    public LoanEligibilityFacade loanEligibilityFacadeForTests() {
        return new LoanEligibilityFacade(
                new InMemoryApplicantRepository(),
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
