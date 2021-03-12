package mm.inventory.domain.items

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition

@AnalyzeClasses(packagesOf = [ItemsArchitectureTest::class])
class ItemsArchitectureTest {

    @ArchTest
    fun `item class cannot use item`(importedClasses: JavaClasses) = ArchRuleDefinition
        .noClasses().that().resideInAPackage("..itemclass")
        .should().dependOnClassesThat().resideInAPackage("..item")
        .check(importedClasses)

    @ArchTest
    fun `manufacturer cannot use item nor itemclass`(importedClasses: JavaClasses) = ArchRuleDefinition
        .noClasses().that().resideInAPackage("..manufacturer")
        .should().dependOnClassesThat().resideInAPackage("..itemclass")
        .orShould().dependOnClassesThat().resideInAPackage("..item")
        .check(importedClasses)
}