package dev.codetoreason.patterns.tactical.result.example.loan;

import dev.codetoreason.patterns.tactical.infra.repository.EntityRepository;

interface ApplicantProfileRepository extends EntityRepository<ApplicantProfile, ApplicantId> {
}

