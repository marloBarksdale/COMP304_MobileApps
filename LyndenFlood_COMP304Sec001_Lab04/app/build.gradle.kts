import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.lyndenflood_comp304sec001_lab04"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lyndenflood_comp304sec001_lab04"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Read API key from secrets.properties
        val secretsPropertiesFile = rootProject.file("secrets.properties")
        val apiKey = if (secretsPropertiesFile.exists()) {
            val secretsProperties = Properties()
            secretsProperties.load(secretsPropertiesFile.inputStream())
            secretsProperties.getProperty("MAPS_API_KEY") ?: ""
        } else {
            ""
        }

        // Debug: Print to check if API key is loaded
        println("🔍 Debug: API Key loaded: ${if (apiKey.isNotEmpty()) "✅ Yes (${apiKey.take(8)}...)" else "❌ No"}")
        println("🔍 Debug: secrets.properties exists: ${secretsPropertiesFile.exists()}")

        manifestPlaceholders["MAPS_API_KEY"] = apiKey
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
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Google Maps & Location Services
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation(libs.play.services.location)

    // Navigation for multiple activities
    implementation(libs.androidx.navigation.compose)

    // For loading images
    implementation("io.coil-kt:coil-compose:2.5.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}