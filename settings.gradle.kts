pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "DroidconIndia"
include(":androidApp")
include(":shared")

enableFeaturePreview("VERSION_CATALOGS")
