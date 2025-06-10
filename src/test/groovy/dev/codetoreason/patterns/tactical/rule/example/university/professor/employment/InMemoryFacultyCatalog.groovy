package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment

import dev.codetoreason.patterns.tactical.infra.repository.InMemoryEntityRepository

class InMemoryFacultyCatalog extends InMemoryEntityRepository<FacultyCatalogEntry, FacultyId> implements FacultyCatalog {
}
