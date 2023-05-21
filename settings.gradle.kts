pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MemoApp"

// app
include(":app")
include(":demo_app_photo_meta_inf")

// feature
include(":features:account")
include(":features:camera")
include(":features:friendship")
include(":features:friends_map")
include(":features:memories")
include(":features:photo_widget")
include(":features:publish_photo")
include(":features:user_profile")
include(":features:feed")
include(":features:chat")
include(":features:chats_list")
include(":features:memories_generation")
include(":features:post_on_map")
include(":features:memories_upload")

// core
include(":core:data")
include(":core:datastore")
include(":core:design")
include(":core:glide")
include(":core:common_models")
include(":core:network")
include(":core:storage")
include(":core:utils")
include(":core:memories_auto_generation")
include(":core:map")
include(":core:location")
include(":core:notifications")
