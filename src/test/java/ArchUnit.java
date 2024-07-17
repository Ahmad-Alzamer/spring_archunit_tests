import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvent;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import static  com.tngtech.archunit.lang.SimpleConditionEvent.*;
import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.*;
import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.*;
import static com.tngtech.archunit.lang.conditions.ArchConditions.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@AnalyzeClasses(packages = "com.aalzamer.ArchunitTest", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchUnit {


    @ArchTest
    public static final ArchRule ControllerRules = classes().that().areAnnotatedWith(RestController.class)
            .should().resideInAPackage("..controllers..")
            .andShould().haveSimpleNameEndingWith("Controller")
            ;
    @ArchTest
    public static final ArchRule ServiceRules = classes().that().areAnnotatedWith(Service.class)
            .should().resideInAPackage("..services..")
            .andShould().haveSimpleNameEndingWith("Service")
            ;
    @ArchTest
    public static final ArchRule RepoRules = classes().that().areAnnotatedWith(Repository.class)
            .should().resideInAPackage("..repos..")
            .andShould().haveSimpleNameEndingWith("Repo")
//            .andShould(not(dependOnClassesThat(resideInAPackage(""))))
            ;


    @ArchTest
    public static final ArchRule crossLayerDependencyRUle = Architectures.layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Controllers").definedBy(annotatedWith(RestController.class))
            .layer("Services").definedBy(annotatedWith(Service.class))
            .layer("Repos").definedBy(annotatedWith(Repository.class))
            .whereLayer("Controllers").mayNotBeAccessedByAnyLayer()
            .whereLayer("Repos").mayNotAccessAnyLayer()
            .whereLayer("Services").mayOnlyBeAccessedByLayers("Controllers")
            .whereLayer("Repos").mayOnlyBeAccessedByLayers("Services")
            ;

    @ArchTest
    public static final ArchRule publicControllerEndpointsShouldLogAtInfoLevel = methods()
            .that()
            .arePublic()
            .and(annotatedWith(GetMapping.class)
                    .or(annotatedWith(PostMapping.class))
                    .or(annotatedWith(DeleteMapping.class))
                    .or(annotatedWith(PutMapping.class))
                    .or(annotatedWith(PatchMapping.class))
                    .or(annotatedWith(RequestMapping.class))
            )
            .should(log("info"));

    private static ArchCondition<? super JavaMethod> log(String level){
        return new ArchCondition<>("log") {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                var logCallFound = method.getMethodCallsFromSelf().stream()
                        .filter(call -> level.equalsIgnoreCase(call.getName()))
                        .anyMatch(call -> call.getTargetOwner().isEquivalentTo(Logger.class));

                if(!logCallFound){
                    events.add(violated(method, ConditionEvent.createMessage(method,"does not log at level:"+level)));
                }


            }
        };
    }


}
