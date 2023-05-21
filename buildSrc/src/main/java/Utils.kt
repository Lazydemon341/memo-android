import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

fun Project.useDiWithKapt() = useDiInternal(true)

fun Project.useDi() = useDiInternal(false)

private fun Project.useDiInternal(withKapt: Boolean = false) {
    if (withKapt) {
        with(pluginManager) {
            apply("dagger.hilt.android.plugin")
            apply("org.jetbrains.kotlin.kapt")
        }
    }

    dependencies {
        add("implementation", DILib.hilt)
        if (withKapt) {
            add("kapt", DILib.kapt)
        }
    }

    if (withKapt) {
        kaptExtension.correctErrorTypes = true
    }
}

private val Project.kaptExtension: KaptExtension
    get() = extensions.getByName("kapt") as KaptExtension
