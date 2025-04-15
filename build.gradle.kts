// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false

    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.maven.publish)
}

val versionCode by extra { 7 }
val versionName by extra { "0.7.0" }
val minSdkVersion by extra { 24 }
val compileSdkVersion by extra { 35 }
val targetSdkVersion by extra { 35 }

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}
