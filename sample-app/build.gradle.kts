import com.securevale.rasp.android.Deps

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = Deps.compileSDKVersion

    defaultConfig {
        applicationId = "com.securevale.rasp.android"
        minSdk = Deps.minSDKVersion
        targetSdk = Deps.targetSDKVersion
        versionCode = Deps.versionCode
        versionName = Deps.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            applicationIdSuffix = ".release"
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    namespace = "com.securevale.rasp.android.sample"

    packagingOptions.jniLibs.keepDebugSymbols += "**/*.so"
}

dependencies {
    implementation(project(":rasp"))
    implementation(Deps.Android.coreKtx)
    implementation(Deps.Android.appcompat)
    implementation(Deps.Android.material)
    implementation(Deps.Android.constraintLayout)
}