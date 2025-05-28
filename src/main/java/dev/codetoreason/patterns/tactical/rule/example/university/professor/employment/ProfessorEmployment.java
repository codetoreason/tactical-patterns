package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment;

import dev.codetoreason.patterns.tactical.capacity.Capacity;

import java.util.Optional;

interface ProfessorEmployment {

    Optional<ProfessorId> employNewAt(FacultyId facultyId, Capacity capacity);
}
