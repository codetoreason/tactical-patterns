package dev.codetoreason.patterns.tactical.result.example.loan;

import dev.codetoreason.patterns.tactical.money.Money;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

import static dev.codetoreason.patterns.tactical.money.Currency.PLN;

@Slf4j
class LoanEligibilityAssessor {

    private static final Money ZERO = Money.zero(PLN);

    private final Collection<ApplicantValidator> validators;
    private final CreditLimitCalculator creditLimitCalculator;

    LoanEligibilityAssessor(
            Collection<ApplicantValidator> validators,
            CreditLimitCalculator creditLimitCalculator
    ) {
        this.validators = validators;
        this.creditLimitCalculator = creditLimitCalculator;
    }

    Money assess(ApplicantProfile profile) {
        for (var validator : validators) {
            var result = validator.validate(profile);
            if (result.isFailure()) {
                log.info(result.message());
                return ZERO;
            }
        }
        return creditLimitCalculator.suggestLimit(profile);
    }
}

