package dev.codetoreason.patterns.tactical.result.example.loan;

import java.util.Optional;

interface ApplicantRepository {

    Optional<ApplicantProfile> findProfileById(ApplicantId id);

}

