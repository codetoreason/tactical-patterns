package dev.codetoreason.patterns.tactical.rule.snippets;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

record FieldsOfStudies(
        Collection<FieldOfStudy> all
) {

    private static final FieldsOfStudies EMPTY = new FieldsOfStudies(Set.of());

    static FieldsOfStudies empty() {
        return EMPTY;
    }

    static FieldsOfStudies of(String... names) {
        return new FieldsOfStudies(Stream.of(names)
                                         .map(FieldOfStudy::of)
                                         .collect(toSet()));
    }

    static Collector<FieldOfStudy, Object, FieldsOfStudies> toFieldsOfStudies() {
        return collectingAndThen(
                toSet(),
                FieldsOfStudies::new
        );
    }

    int count() {
        return all.size();
    }

    int numberOfMatched(FieldsOfStudies fieldsOfStudies) {
        return (int) all.stream()
                        .filter(fieldsOfStudies.all::contains)
                        .count();
    }

    boolean matchesAllOf(FieldsOfStudies fieldsOfStudies) {
        return all.containsAll(fieldsOfStudies.all);
    }

    FieldsOfStudies add(FieldOfStudy fieldOfStudyId) {
        return new FieldsOfStudies(
                Stream.concat(all.stream(), Stream.of(fieldOfStudyId))
                      .collect(toSet())
        );
    }
}
