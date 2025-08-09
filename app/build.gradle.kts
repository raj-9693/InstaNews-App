import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.protej.instanews"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.protej.instanews"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures{
        viewBinding=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.cronet.embedded)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation (libs.androidx.recyclerview)
//picasso
    implementation (libs.picasso)

    // Retrofit
    implementation (libs.retrofit)

// Gson Converter
    implementation (libs.converter.gson)

    implementation (libs.github.glide)
    kapt (libs.compiler)

    implementation(libs.lottie)

        implementation (libs.androidx.room.runtime)

// Room compiler (Kotlin के लिए kapt यूज़ करना जरूरी है)

    kapt (libs.androidx.room.compiler)


    implementation (libs.androidx.room.ktx) // ✅ यह जरूरी है suspend के लिए

    implementation(libs.material.v1110)

    implementation(libs.shimmer)





}