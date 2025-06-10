package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public record FieldsOfStudies(
        Collection<FieldOfStudy> all
) {

    private static final FieldsOfStudies EMPTY = new FieldsOfStudies(Set.of());

    public static FieldsOfStudies empty() {
        return EMPTY;
    }

    public static FieldsOfStudies of(String... names) {
        return new FieldsOfStudies(Stream.of(names)
                                         .map(FieldOfStudy::of)
                                         .collect(toSet()));
    }

    public static Collector<FieldOfStudy, Object, FieldsOfStudies> toFieldsOfStudies() {
        return collectingAndThen(
                toSet(),
                FieldsOfStudies::new
        );
    }

    public int count() {
        return all.size();
    }

    public int numberOfMatched(FieldsOfStudies fieldsOfStudies) {
        return (int) all.stream()
                        .filter(fieldsOfStudies.all::contains)
                        .count();
    }

    public boolean matchesAllOf(FieldsOfStudies fieldsOfStudies) {
        return all.containsAll(fieldsOfStudies.all);
    }

    public FieldsOfStudies add(FieldOfStudy fieldOfStudyId) {
        return new FieldsOfStudies(
                Stream.concat(all.stream(), Stream.of(fieldOfStudyId))
                      .collect(toSet())
        );
    }
}
