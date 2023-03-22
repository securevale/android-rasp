// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.4.0" apply false
    id("com.android.library") version "7.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.7.22" apply false
    id("org.jetbrains.kotlin.jvm") version "1.7.22" apply false
    id("io.gitlab.arturbosch.detekt") version "1.22.0" apply false
    id("org.jetbrains.dokka") version "1.7.20" apply false
    id("com.vanniktech.maven.publish") version "0.23.1"
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}
