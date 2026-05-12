plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.areyouAlright"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.areyouAlright"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_BASE_URL", "\"http://localhost:5000\"")
            buildConfigField("String", "APP_NAME", "\"Are You Alright (Dev)\"")
            manifestPlaceholders["appName"] = "Are You Alright (Dev)"
            manifestPlaceholders["usesCleartextTraffic"] = true
        }
        release {
            buildConfigField("String", "API_BASE_URL", "\"https://are-you-alive-vjxi.onrender.com\"")
            buildConfigField("String", "APP_NAME", "\"Are You Alright\"")
            manifestPlaceholders["appName"] = "Are You Alright"
            manifestPlaceholders["usesCleartextTraffic"] = false
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
