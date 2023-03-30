import com.securevale.rasp.android.Deps
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
    compileSdk = Deps.compileSDKVersion

    defaultConfig {
        minSdk = Deps.minSDKVersion
        targetSdk = Deps.targetSDKVersion

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    namespace = "com.securevale.rasp.android"

    lint {
        abortOnError = true
        warningsAsErrors = true
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
    implementation(Deps.Android.coreKtx)
    implementation(Deps.Android.appcompat)
    implementation(Deps.Android.material)

    debugImplementation(Deps.Debug.leakCanary)
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
    version = "0.2.0"
    coordinates("com.securevale", "rasp-android", "0.2.0")

    publishToMavenCentral(SonatypeHost.S01, true)
    // TODO need to be resolved during release task
//    signAllPublications()

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
