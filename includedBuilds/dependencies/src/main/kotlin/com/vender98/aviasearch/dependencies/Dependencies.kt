package com.vender98.aviasearch.dependencies

object Versions {
    const val minSdk = 23
    const val compileSdk = 30
    const val targetSdk = 30
}

object Dependencies {

    object AndroidX {
        const val ktxCore = "androidx.core:core-ktx:1.5.0"
        const val appcompat = "androidx.appcompat:appcompat:1.3.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    }

    object Google {
        const val material = "com.google.android.material:material:1.3.0"
    }

    const val desugarJdkLibs = "com.android.tools:desugar_jdk_libs:1.1.5"
}