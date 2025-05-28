package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment;

import dev.codetoreason.patterns.tactical.infra.repository.EntityRepository;

interface FacultyCatalog extends EntityRepository<FacultyCatalogEntry, FacultyId> {
}
