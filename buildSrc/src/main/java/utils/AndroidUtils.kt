import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

val Project.androidExtension: BaseExtension
    get() {
        val androidExtension = project.extensions.getByName("android")
        return androidExtension as BaseExtension
    }

fun Project.setPackage(packageName: String) {
    androidExtension.namespace = packageName
}

fun Project.useCompose() {
    androidExtension.apply {
        buildFeatures.compose = true
        composeOptions {
            kotlinCompilerExtensionVersion = Versions.kotlinCompilerExtension
        }
    }
    project.dependencies {
        add("implementation", platform(ComposeLib.bom))
        add("implementation", ComposeLib.foundation)
        add("implementation", ComposeLib.material3)
        add("implementation", ComposeLib.runtime)
        add("implementation", ComposeLib.ui)
        add("implementation", ComposeLib.uiTooling)
        add("debugImplementation", ComposeLib.uiToolingDebug)
    }
}
