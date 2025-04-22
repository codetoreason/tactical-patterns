package dev.codetoreason.patterns.tactical.result.example.loan

class InMemoryApplicantRepository implements ApplicantRepository {

    private final def storage = new HashMap<ApplicantId, ApplicantProfile>()

    @Override
    Optional<ApplicantProfile> findProfileById(ApplicantId id) {
        return Optional.ofNullable(storage.get(id))
    }

    void save(ApplicantProfile profile) {
        storage.put(profile.id(), profile)
    }
}
