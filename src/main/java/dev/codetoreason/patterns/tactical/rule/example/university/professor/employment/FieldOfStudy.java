package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment;

public record FieldOfStudy(
        String name
) {

    public FieldOfStudy {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must be non-null and non-blank");
        }
    }

    public static FieldOfStudy of(String name) {
        return new FieldOfStudy(name);
    }
}
