package com.vender98.aviasearch.dependencies

object Versions {
    const val minSdk = 23
    const val compileSdk = 30
    const val targetSdk = 30

    const val toothpick = "3.1.0"
    const val kotlinCoroutines = "1.5.0"
}

object Dependencies {

    object Kotlin {
        const val coroutines =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
        const val coroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.3.2"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.3"
        const val appcompat = "androidx.appcompat:appcompat:1.3.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    }

    object Google {
        const val material = "com.google.android.material:material:1.3.0"
    }

    object DI {
        const val toothpickRuntime =
            "com.github.stephanenicolas.toothpick:toothpick-runtime:${Versions.toothpick}"
        const val toothpickCompiler =
            "com.github.stephanenicolas.toothpick:toothpick-compiler:${Versions.toothpick}"
    }

    const val desugarJdkLibs = "com.android.tools:desugar_jdk_libs:1.1.5"
    const val viewBindingPropertyDelegate =
        "com.kirich1409.viewbindingpropertydelegate:viewbindingpropertydelegate:1.3.1"
}