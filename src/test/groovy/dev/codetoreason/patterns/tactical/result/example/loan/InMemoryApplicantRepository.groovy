package dev.codetoreason.patterns.tactical.result.example.loan

import dev.codetoreason.patterns.tactical.infra.repository.InMemoryEntityRepository

class InMemoryApplicantRepository extends InMemoryEntityRepository<ApplicantProfile, ApplicantId>
        implements ApplicantProfileRepository {
}
