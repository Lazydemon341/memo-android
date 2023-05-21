package plugins

import AppConfig
import androidExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Project

class AppModulePlugin : BaseAndroidModulePlugin(isLibrary = false) {

    override fun apply(project: Project) {
        super.apply(project)
        project.androidExtension.apply {
            setApplicationId()
        }
    }

    private fun BaseExtension.setApplicationId() {
        defaultConfig {
            applicationId = AppConfig.appId
        }
    }
}
