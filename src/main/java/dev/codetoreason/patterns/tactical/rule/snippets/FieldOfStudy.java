package dev.codetoreason.patterns.tactical.rule.snippets;

record FieldOfStudy(
        String name
) {

    FieldOfStudy {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must be non-null and non-blank");
        }
    }

    static FieldOfStudy of(String name) {
        return new FieldOfStudy(name);
    }
}
