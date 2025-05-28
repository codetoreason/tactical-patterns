package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment;

import dev.codetoreason.patterns.tactical.capacity.Capacity;
import dev.codetoreason.patterns.tactical.quantity.Quantity;
import dev.codetoreason.patterns.tactical.rule.Rules;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@RequiredArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProfessorEmploymentFacade {

    ProfessorEmploymentConfig config;
    Rules<ProfessorEmploymentContext> employmentRules;
    FacultyCatalog facultyCatalog;
    ProfessorCatalog professorCatalog;
    ProfessorEmployment professorEmployment;

    public Optional<ProfessorId> employProfessor(ProfessorEmploymentApplication application) {
        log.info("Employing professor with application {}", application);
        var facultyId = application.facultyId();
        var faculty = facultyCatalog.getById(facultyId);
        var context = new ProfessorEmploymentContext(application, faculty);
        var result = employmentRules.examine(context);
        if (result.isFailure()) {
            log.info(result.message());
            return Optional.empty();
        }
        var professorCapacity = Capacity.with(Quantity.of(
                config.maxCourseLeaderships()
        ));
        var maybeEmployed = professorEmployment.employNewAt(facultyId, professorCapacity);
        maybeEmployed.ifPresentOrElse(
                professorId -> professorCatalog.save(
                        new ProfessorCatalogEntry(
                                professorId,
                                application.professorName(),
                                application.fieldsOfStudies()
                        )
                ),
                () -> log.info("Professor was not employed at faculty {}", facultyId)
        );
        return maybeEmployed;
    }
}
