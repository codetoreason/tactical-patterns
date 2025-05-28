package dev.codetoreason.patterns.tactical.result.example.loan;

import java.util.Optional;

public class LoanEligibilityFacade {

    private final ApplicantProfileRepository applicantRepository;
    private final LoanEligibilityAssessor loanEligibilityAssessor;

    LoanEligibilityFacade(
            ApplicantProfileRepository applicantRepository,
            LoanEligibilityAssessor loanEligibilityAssessor
    ) {
        this.applicantRepository = applicantRepository;
        this.loanEligibilityAssessor = loanEligibilityAssessor;
    }

    public Optional<LoanEligibilityResult> assessEligibility(ApplicantId id) {
        return applicantRepository.findById(id)
                                  .map(loanEligibilityAssessor::assess)
                                  .map(loanEligibility -> new LoanEligibilityResult(id, loanEligibility));
    }
}

