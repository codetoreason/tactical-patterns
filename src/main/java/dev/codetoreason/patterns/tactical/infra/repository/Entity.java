package dev.codetoreason.patterns.tactical.infra.repository;

/**
 * A base interface for domain entities that have a stable identifier.
 * <p>
 * All entities managed by a {@link EntityRepository} must implement this interface
 * to provide consistent identity semantics across the domain model.
 *
 * <p>This abstraction allows the repository to enforce rules around uniqueness,
 * identity-based retrieval, and equivalence by business key.
 *
 * @param <ID> the type of the identifier
 */
public interface Entity<ID> {

    /**
     * Returns the stable identifier of this entity.
     * This ID is used to uniquely distinguish this entity from others.
     *
     * @return the identifier of the entity
     */
    ID id();
}
