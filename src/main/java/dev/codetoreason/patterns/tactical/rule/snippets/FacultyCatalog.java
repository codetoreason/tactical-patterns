package dev.codetoreason.patterns.tactical.rule.snippets;

import dev.codetoreason.patterns.tactical.infra.repository.EntityRepository;

interface FacultyCatalog extends EntityRepository<FacultyCatalogEntry, FacultyId> {
}
