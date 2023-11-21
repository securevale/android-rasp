import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish")
}

android {
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdkVersion"] as Int
        targetSdk = rootProject.extra["targetSdkVersion"] as Int

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFile("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            consumerProguardFiles( "consumer-rules.pro")
        }

        getByName("debug") {
            isMinifyEnabled = false
            enableUnitTestCoverage = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    namespace = "com.securevale.rasp.android"

    lint {
        abortOnError = true
        warningsAsErrors = true
        disable += "GradleDependency"
    }

    detekt {
        config = files("$projectDir/config/detekt.yml")
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            unitTests.isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(libs.androidx.corektx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)

    debugImplementation(libs.square.leakcanary)
}

tasks.dokkaHtml.configure { configureDokka(this, "dokkaHtml") }
tasks.dokkaJavadoc.configure { configureDokka(this, "dokkaJavadoc") }

fun configureDokka(dokkaTask: DokkaTask, outputDir: String) = dokkaTask.apply {
    outputDirectory.set(buildDir.resolve(outputDir))

    dokkaSourceSets {
        configureEach {
            documentedVisibilities.set(
                setOf(
                    Visibility.PUBLIC,
                    Visibility.PRIVATE,
                    Visibility.PROTECTED,
                    Visibility.INTERNAL,
                    Visibility.PACKAGE
                )
            )

            skipEmptyPackages.set(true)
        }
    }
}

mavenPublishing {
    group = "com.securevale.rasp"
    version = "0.4.0"
    coordinates("com.securevale", "rasp-android", "0.4.0")

    publishToMavenCentral(SonatypeHost.S01, true)
    signAllPublications()

    pom {
        name.set("Android RASP")
        description.set("Runtime Application Self Protection library for Android.")
        inceptionYear.set("2023")
        url.set("https://github.com/securevale/android-rasp")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("bmaciejm")
                name.set("Bartosz Macięga")
                url.set("https://github.com/bmaciejm")
            }
        }

        scm {
            url.set("https://github.com/securevale/android-rasp")
            connection.set("scm:git:git://github.com/securevale/android-rasp.git")
            developerConnection.set("scm:git:ssh://git@github.com/securevale/android-rasp.git")
        }
    }
}
