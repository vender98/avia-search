plugins {
    id("dependencies")
}

buildscript {
    extra["kotlin_version"] = "1.4.32"

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${project.extra["kotlin_version"]}")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
    }
}

tasks.register<Delete>("clean") {
    delete = setOf(rootProject.buildDir)
}