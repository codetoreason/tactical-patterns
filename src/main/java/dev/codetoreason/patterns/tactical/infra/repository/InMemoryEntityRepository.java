package dev.codetoreason.patterns.tactical.infra.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Simple in-memory implementation of {@link EntityRepository} for use in examples and tests.
 * <p>
 * Stores entities in a local {@link HashMap} using their {@link Entity#id()} as keys.
 * Intended for showcasing business-oriented code patterns without external persistence.
 *
 * <p><strong>Note:</strong> This implementation is <em>not thread-safe</em>
 * and should only be used in single-threaded contexts such as unit tests or documentation examples.
 *
 * @param <E>  the type of the entity
 * @param <ID> the type of the entity identifier
 */
public abstract class InMemoryEntityRepository<E extends Entity<ID>, ID> implements EntityRepository<E, ID> {

    private final Map<ID, E> repo = new HashMap<>();

    /**
     * Stores or replaces the given entity in memory.
     *
     * @param e the entity to save
     */
    @Override
    public void save(E e) {
        repo.put(e.id(), e);
    }

    /**
     * Attempts to find an entity by its identifier.
     *
     * @param id the identifier of the entity
     * @return an {@link Optional} containing the entity if found, or empty if not
     */
    @Override
    public Optional<E> findById(ID id) {
        return Optional.ofNullable(repo.get(id));
    }

    /**
     * Returns all entities that match the given predicate.
     *
     * @param predicate the filtering predicate
     * @return a list of matching entities
     */
    public List<E> findAllBy(Predicate<E> predicate) {
        return repo.values()
                   .stream()
                   .filter(predicate)
                   .toList();
    }
}
