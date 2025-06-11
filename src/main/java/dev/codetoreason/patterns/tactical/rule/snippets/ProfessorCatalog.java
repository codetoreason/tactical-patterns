package dev.codetoreason.patterns.tactical.rule.snippets;

import dev.codetoreason.patterns.tactical.infra.repository.EntityRepository;

interface ProfessorCatalog extends EntityRepository<ProfessorCatalogEntry, ProfessorId> {
}
