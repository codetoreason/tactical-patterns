package dev.codetoreason.patterns.tactical.rule.example.university.professor.employment

import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.FacultyCatalogEntry.FacultyCatalogEntryBuilder
import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentApplication.ProfessorEmploymentApplicationBuilder
import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentContext.ProfessorEmploymentContextBuilder
import static dev.codetoreason.patterns.tactical.rule.example.university.professor.employment.ProfessorEmploymentContext.builder
import static groovy.lang.Closure.DELEGATE_FIRST

class ProfessorEmploymentFixture {

    static final def IT_FACULTY_NAME = "IT Faculty"
    static final def ZUCK = "Zuck"

    private static final def IT_FACULTY_ID = new FacultyId("IT")

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
            @DelegatesTo(value = ApplicationDsl, strategy = DELEGATE_FIRST) Closure<?> modifier
    ) {
        def wrapper = new ApplicationDsl(newApplicationBuilder())
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


    private static FieldsOfStudies fields(String... names) {
        new FieldsOfStudies(names.collect {
            new FieldOfStudy(it)
        })
    }

    private static class ApplicationDsl {

        private final ProfessorEmploymentApplicationBuilder delegate

        ApplicationDsl(ProfessorEmploymentApplicationBuilder delegate) {
            this.delegate = delegate
        }

        void yearsOfExperience(int value) {
            delegate.yearsOfExperience(value)
        }

        void fieldsOfStudies(String... names) {
            delegate.fieldsOfStudies(fields(names))
        }

        void noFields() { delegate.fieldsOfStudies(FieldsOfStudies.empty()) }

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
            delegate.fieldsOfStudies(fields(names))
        }

        FacultyCatalogEntryBuilder unwrap() {
            return delegate
        }
    }
}
