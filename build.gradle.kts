plugins {
    id("com.google.dagger.hilt.android") version Versions.hilt apply false
    id("com.google.devtools.ksp") version Versions.ksp apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

task("setupGitHook", type = Copy::class) {
    from(File(rootProject.rootDir, "tools/pre-commit"))
    into { File(rootProject.rootDir, ".git/hooks") }
    fileMode = 0b111101101
}
