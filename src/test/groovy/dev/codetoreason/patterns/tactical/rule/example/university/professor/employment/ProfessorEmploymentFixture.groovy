package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment

import dev.codetoreason.patterns.tactical.capacity.Capacity
import dev.codetoreason.patterns.tactical.result.OperationResult
import dev.codetoreason.patterns.tactical.rule.Rules
import org.mockito.Mockito

import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.FacultyCatalogEntry.FacultyCatalogEntryBuilder
import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentApplication.ProfessorEmploymentApplicationBuilder
import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentContext.ProfessorEmploymentContextBuilder
import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentContext.builder
import static groovy.lang.Closure.DELEGATE_FIRST
import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.when

class ProfessorEmploymentFixture {

    static final def IT_FACULTY_NAME = "IT Faculty"
    static final def IT_FACULTY_ID = new FacultyId("IT")
    static final def ZUCK = "Zuck"
    static final def DEFAULT_CONFIG = ProfessorEmploymentConfig.builder()
                                                               .minYearsOfExperience(5)
                                                               .minMatchedFieldsOfStudy(3)
                                                               .maxCourseLeaderships(4)
                                                               .build()

    private final FacultyCatalog facultyCatalog = new InMemoryFacultyCatalog()
    private final ProfessorCatalog professorCatalog = new InMemoryProfessorCatalog()

    private final ProfessorEmployment professorEmploymentMock = Mockito.mock(ProfessorEmployment)
    private final Rules<ProfessorEmploymentContext> rulesMock = Mockito.mock(Rules)

    static ProfessorEmploymentFixture create() {
        new ProfessorEmploymentFixture()
    }

    static ProfessorEmploymentContext contextWith(
            @DelegatesTo(value = ProfessorEmploymentContextBuilder, strategy = DELEGATE_FIRST) Closure<?> modifier
    ) {
        def builder = builder()
        modifier.delegate = builder
        modifier.resolveStrategy = DELEGATE_FIRST
        modifier.call()
        builder.build()
    }

    static ProfessorEmploymentApplication applicationWith(
            @DelegatesTo(value = ProfessorApplicationDsl, strategy = DELEGATE_FIRST) Closure<?> modifier
    ) {
        def wrapper = new ProfessorApplicationDsl(newApplicationBuilder())
        modifier.delegate = wrapper
        modifier.resolveStrategy = DELEGATE_FIRST
        modifier.call()
        wrapper.unwrap().build()
    }

    static FacultyCatalogEntry facultyWith(
            @DelegatesTo(value = FacultyDsl, strategy = DELEGATE_FIRST) Closure<?> modifier
    ) {
        def wrapper = new FacultyDsl(newFacultyBuilder())
        modifier.delegate = wrapper
        modifier.resolveStrategy = DELEGATE_FIRST
        modifier.call()
        wrapper.unwrap().build()
    }

    ProfessorEmploymentFixture withRulesExaminedSuccessfully() {
        when(rulesMock.examine(any(ProfessorEmploymentContext)))
                .thenReturn(OperationResult.successful())
        this
    }

    ProfessorEmploymentFixture withRulesExaminedNegatively() {
        when(rulesMock.examine(any(ProfessorEmploymentContext)))
                .thenReturn(OperationResult.failed("failed"))
        this
    }

    ProfessorEmploymentFixture withSuccessfulEmploymentOperation() {
        when(professorEmploymentMock.employNewAt(eq(IT_FACULTY_ID), any(Capacity)))
                .thenReturn(Optional.of(ProfessorId.newOne()))
        this
    }

    ProfessorEmploymentFixture withFailedEmploymentOperation() {
        when(professorEmploymentMock.employNewAt(eq(IT_FACULTY_ID), any(Capacity)))
                .thenReturn(Optional.empty())
        this
    }

    ProfessorEmploymentFixture withFacultyContainingFieldsOfStudies(String... names) {
        facultyCatalog.save(
                FacultyCatalogEntry.builder()
                                   .id(IT_FACULTY_ID)
                                   .name(IT_FACULTY_NAME)
                                   .fieldsOfStudies(FieldsOfStudies.of(names))
                                   .build()
        )
        this
    }

    ProfessorEmploymentFacade buildFacade() {
        new ProfessorEmploymentFacade(
                DEFAULT_CONFIG,
                rulesMock,
                facultyCatalog,
                professorCatalog,
                professorEmploymentMock
        )
    }

    ProfessorCatalogEntry getProfessorById(ProfessorId professorId) {
        professorCatalog.getById(professorId)
    }

    boolean noProfessorPersisted() {
        professorCatalog.findAll()
                        .isEmpty()
    }

    private static ProfessorEmploymentApplicationBuilder newApplicationBuilder() {
        ProfessorEmploymentApplication.builder()
                                      .facultyId(IT_FACULTY_ID)
                                      .professorName(ZUCK)
    }

    private static FacultyCatalogEntryBuilder newFacultyBuilder() {
        FacultyCatalogEntry.builder()
                           .id(IT_FACULTY_ID)
                           .name(IT_FACULTY_NAME)
    }

    private static class ProfessorApplicationDsl {

        private final ProfessorEmploymentApplicationBuilder delegate

        ProfessorApplicationDsl(ProfessorEmploymentApplicationBuilder delegate) {
            this.delegate = delegate
        }

        void yearsOfExperience(int value) {
            delegate.yearsOfExperience(value)
        }

        void fieldsOfStudies(String... names) {
            delegate.fieldsOfStudies(FieldsOfStudies.of(names))
        }

        ProfessorEmploymentApplicationBuilder unwrap() {
            delegate
        }
    }

    private static class FacultyDsl {

        private final FacultyCatalogEntryBuilder delegate

        FacultyDsl(FacultyCatalogEntryBuilder delegate) {
            this.delegate = delegate
        }

        void fieldsOfStudies(String... names) {
            delegate.fieldsOfStudies(FieldsOfStudies.of(names))
        }

        FacultyCatalogEntryBuilder unwrap() {
            return delegate
        }
    }
}
