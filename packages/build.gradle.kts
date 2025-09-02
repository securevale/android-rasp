import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.securevale.rasp.android.packages"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdkVersion"] as Int
        targetSdk = rootProject.extra["targetSdkVersion"] as Int
    }

    buildTypes {
        release {
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
}

mavenPublishing {

    group = "com.securevale.rasp"
    version = rootProject.extra["versionName"] as String
    coordinates("com.securevale", "rasp-packages", rootProject.extra["versionName"] as String)

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, true)
    signAllPublications()

    pom {
        name.set("Android RASP Packages")
        description.set("Runtime Application Self Protection library for Android with package queries.")
        inceptionYear.set("2025")
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