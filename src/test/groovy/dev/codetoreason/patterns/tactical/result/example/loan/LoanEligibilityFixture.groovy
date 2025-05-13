package dev.codetoreason.patterns.tactical.result.example.loan

class LoanEligibilityFixture {

    private final def applicantRepo = new InMemoryApplicantRepository()
    private final def factory = new LoanEligibilityFacadeFactory()

    static LoanEligibilityFacade withoutApplicants() {
        new LoanEligibilityFixture().build()
    }

    static LoanEligibilityFixture create() {
        new LoanEligibilityFixture()
    }

    LoanEligibilityFixture withApplicant(ApplicantProfile applicantProfile) {
        applicantRepo.save(applicantProfile)
        this
    }

    LoanEligibilityFacade build() {
        factory.loanEligibilityFacadeForTests(applicantRepo)
    }
}
