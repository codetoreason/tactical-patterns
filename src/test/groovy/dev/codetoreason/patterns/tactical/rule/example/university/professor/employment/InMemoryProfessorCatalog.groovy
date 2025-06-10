package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment

import dev.codetoreason.patterns.tactical.infra.repository.InMemoryEntityRepository

class InMemoryProfessorCatalog extends InMemoryEntityRepository<ProfessorCatalogEntry, ProfessorId> implements ProfessorCatalog {
}
