package dev.codetoreason.patterns.tactical.infra.repository;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * A minimalistic repository interface for accessing and persisting domain entities.
 * <p>
 * This interface is intended for examples that demonstrate business-oriented code patterns.
 * It provides only the core methods required for typical entity lifecycle management:
 * saving, finding by identifier, and fail-fast retrieval.
 *
 * @param <E>  the type of entity
 * @param <ID> the type of the entity identifier
 */
public interface EntityRepository<E, ID> {

    /**
     * Saves the given entity.
     * <p>
     * If the entity is new, it should be inserted; if it already exists, it should be updated.
     *
     * @param e the entity to save
     */
    void save(E e);

    /**
     * Finds an entity by its identifier.
     *
     * @param id the identifier of the entity
     * @return an {@link Optional} containing the entity if found, or empty if not found
     */
    Optional<E> findById(ID id);

    /**
     * Retrieves an entity by its identifier or throws if not found.
     * <p>
     * This is a fail-fast alternative to {@link #findById(Object)}. It should be used in business logic
     * when the absence of the entity indicates an error or an invalid state.
     *
     * @param id the identifier of the entity
     * @return the entity if found
     * @throws NoSuchElementException if the entity is not found
     */
    default E getById(ID id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("Entity with ID " + id + " not found"));
    }
}
