package dev.codetoreason.patterns.tactical.rule.snippets;

import dev.codetoreason.patterns.tactical.capacity.Capacity;
import dev.codetoreason.patterns.tactical.quantity.Quantity;
import dev.codetoreason.patterns.tactical.rule.Rules;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Deprecated
@Slf4j
@RequiredArgsConstructor(access = PACKAGE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ProfessorEmploymentLegacyFacade {

    ProfessorEmploymentConfig config;
    FacultyCatalog facultyCatalog;
    ProfessorCatalog professorCatalog;
    ProfessorEmployment professorEmployment;

    Optional<ProfessorId> employProfessor(ProfessorEmploymentApplication application) {
        log.info("Employing professor with application {}", application);
        var facultyId = application.facultyId();
        var professorName = application.professorName();
        if (application.yearsOfExperience() < config.minYearsOfExperience()) {
            log.info("{} cannot be employed at {}: too small experience", professorName, facultyId);
            return Optional.empty();
        }
        var faculty = facultyCatalog.getById(facultyId);
        var minMatchedFieldsOfStudy = config.minMatchedFieldsOfStudy();
        var professorFieldsOfStudy = application.fieldsOfStudies();
        var facultyFieldsOfStudy = faculty.fieldsOfStudies();
        var facultyHasFewFieldsOfStudies = faculty.fieldsOfStudiesNumber() < minMatchedFieldsOfStudy;
        var fieldsOfStudiesMatched = facultyHasFewFieldsOfStudies
                ? professorFieldsOfStudy.matchesAllOf(facultyFieldsOfStudy)
                : professorFieldsOfStudy.numberOfMatched(facultyFieldsOfStudy) >= minMatchedFieldsOfStudy;
        if (!fieldsOfStudiesMatched) {
            log.info("{} cannot be employed at {}: fields of studies not matched", professorName, facultyId);
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
                                professorName,
                                application.fieldsOfStudies()
                        )
                ),
                () -> log.info("Professor was not employed at faculty {}", facultyId)
        );
        return maybeEmployed;
    }
}
