package me.tisana.miniblog;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("me.tisana.miniblog");

        noClasses()
            .that()
                .resideInAnyPackage("me.tisana.miniblog.service..")
            .or()
                .resideInAnyPackage("me.tisana.miniblog.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..me.tisana.miniblog.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
