plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)


    kotlin("kapt")
}

import java.util.Properties
import java.io.File
val apiKeyProps = Properties().apply {
    val file = rootProject.file("apikeys.properties")
    if (file.exists()) {
        file.inputStream().use { this.load(it) }
    }
}



android {
    namespace = "com.example.lyndenflood_comp304lab3_ex1"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.lyndenflood_comp304lab3_ex1"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        buildConfigField(
            "String",
            "API_NINJAS_KEY",
            "\"${apiKeyProps["API_NINJAS_KEY"]}\""
        )





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
    buildFeatures {
        compose = true
        buildConfig =true
    }
}

dependencies {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)


    implementation(libs.kotlinx.coroutines.android)


    implementation(libs.androidx.room.runtime)
    kapt("androidx.room:room-compiler:2.7.2")
    implementation(libs.androidx.room.ktx)
}


