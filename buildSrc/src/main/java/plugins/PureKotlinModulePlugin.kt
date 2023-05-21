package plugins

import KotlinLib
import LogLib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class PureKotlinModulePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.applyKotlinPlugin()
        project.applyKtLintPlugin()
        project.addDefaultDependencies()
    }

    private fun Project.applyKotlinPlugin() {
        project.plugins.apply("kotlin")
    }

    private fun Project.applyKtLintPlugin() {
        plugins.apply("org.jlleitschuh.gradle.ktlint")
    }

    private fun Project.addDefaultDependencies() {
        dependencies {
            add("implementation", KotlinLib.std)
            add("implementation", LogLib.timber)
        }
    }
}
