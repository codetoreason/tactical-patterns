package dev.codetoreason.patterns.tactical.infra.repository;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * A minimal repository interface for accessing and persisting domain entities.
 * <p>
 * Designed for educational and business-oriented code examples,
 * this interface defines only the essential operations for managing entity lifecycle:
 * saving, optional retrieval, and fail-fast access by identifier.
 *
 * <p>All entities must implement the {@link Entity} interface to ensure identity semantics.
 *
 * @param <E>  the type of the entity
 * @param <ID> the type of the entity identifier
 */
public interface EntityRepository<E extends Entity<ID>, ID> {

    /**
     * Saves the given entity.
     * <p>
     * If the entity is new, it should be inserted.
     * If it already exists, it should be updated.
     *
     * @param e the entity to save
     */
    void save(E e);

    /**
     * Tries to find an entity by its identifier.
     *
     * @param id the identifier of the entity
     * @return an {@link Optional} containing the entity if found, or empty if not found
     */
    Optional<E> findById(ID id);

    /**
     * Retrieves an entity by its identifier or fails fast if not found.
     * <p>
     * This is a shortcut for {@code findById(id).orElseThrow(...)} and should be used
     * in business logic where the absence of the entity is considered a violation of invariants.
     *
     * @param id the identifier of the entity
     * @return the entity
     * @throws NoSuchElementException if the entity is not found
     */
    default E getById(ID id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("Entity with ID " + id + " not found"));
    }
}
