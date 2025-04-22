package dev.codetoreason.patterns.tactical.result.example.loan;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class InMemoryApplicantRepository implements ApplicantRepository {

    private final Map<ApplicantId, ApplicantProfile> storage = new HashMap<>();

    @Override
    public Optional<ApplicantProfile> findProfileById(ApplicantId id) {
        return Optional.ofNullable(storage.get(id));
    }

    void save(ApplicantProfile profile) {
        storage.put(profile.id(), profile);
    }
}
