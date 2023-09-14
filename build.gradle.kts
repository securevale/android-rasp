// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("com.android.library") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.0" apply false
    id("org.jetbrains.dokka") version "1.8.20" apply false
    id("com.vanniktech.maven.publish") version "0.25.2"
}

val versionCode by extra { 3 }
val versionName by extra { "0.3.0" }
val minSdkVersion by extra { 23 }
val compileSdkVersion by extra { 33 }
val targetSdkVersion by extra { 33 }

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}
