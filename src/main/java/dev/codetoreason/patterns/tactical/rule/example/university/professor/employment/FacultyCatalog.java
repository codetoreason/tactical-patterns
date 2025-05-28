package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment;

import java.util.Optional;

interface FacultyCatalog {

    Optional<FacultyCatalogEntry> findById(FacultyId id);

    default FacultyCatalogEntry getById(FacultyId id) {
        return findById(id).orElseThrow();
    }
}
