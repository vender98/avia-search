import com.vender98.aviasearch.dependencies.Dependencies
import com.vender98.aviasearch.dependencies.Versions

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    compileSdkVersion(Versions.compileSdk)

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    defaultConfig {
        applicationId = "com.vender98.aviasearch"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)

        versionName = "1.0"
        versionCode = 1
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlin_version"]}")
    implementation(Dependencies.Kotlin.coroutines)
    implementation(Dependencies.Kotlin.coroutinesAndroid)

    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.fragmentKtx)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.AndroidX.constraintLayout)

    implementation(Dependencies.Google.material)

    kapt(Dependencies.DI.toothpickCompiler)
    implementation(Dependencies.DI.toothpickRuntime)

    implementation(Dependencies.Network.okHttp)
    implementation(Dependencies.Network.retrofit)
    implementation(Dependencies.Network.retrofitConverterMoshi)
    implementation(Dependencies.Network.retrofitCoroutinesAdapter)

    kapt(Dependencies.Serialization.moshiCodegen)
    implementation(Dependencies.Serialization.moshi)

    implementation(Dependencies.viewBindingPropertyDelegate)
    implementation(Dependencies.flowBinding)

    coreLibraryDesugaring(Dependencies.desugarJdkLibs)
}