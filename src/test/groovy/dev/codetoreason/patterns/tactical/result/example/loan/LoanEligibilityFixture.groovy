package dev.codetoreason.patterns.tactical.result.example.loan

class LoanEligibilityFixture {

    private final def applicantRepo = new InMemoryApplicantRepository()
    private final def config = new LoanEligibilityFacadeConfig()

    static LoanEligibilityFacade withoutApplicants() {
        new LoanEligibilityFixture().build()
    }

    static LoanEligibilityFixture create() {
        return new LoanEligibilityFixture()
    }

    LoanEligibilityFixture withApplicant(ApplicantProfile applicantProfile) {
        applicantRepo.save(applicantProfile)
        this
    }

    LoanEligibilityFacade build() {
        config.loanEligibilityFacadeForTests(applicantRepo)
    }
}
