buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        val libs = project.extensions.getByName("libs") as org.gradle.accessors.dm.LibrariesForLibs
        classpath(libs.bundles.gradlePlugins)
        classpath(kotlin("gradle-plugin", libs.versions.kotlin.get()))
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}