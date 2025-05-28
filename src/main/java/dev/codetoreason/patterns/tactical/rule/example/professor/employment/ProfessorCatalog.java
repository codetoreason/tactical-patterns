package dev.codetoreason.patterns.tactical.rule.example.professor.employment;

import java.util.Optional;

interface ProfessorCatalog {

    void save(ProfessorCatalogEntry professor);

    Optional<ProfessorCatalogEntry> findById(ProfessorId id);

    default ProfessorCatalogEntry getBtId(ProfessorId id) {
        return findById(id).orElseThrow();
    }
}
