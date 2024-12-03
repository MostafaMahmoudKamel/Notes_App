plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services") //firebase service

    id("com.google.dagger.hilt.android") //hilt dagger
    id("com.google.devtools.ksp") //ksp

    //compose compiler
    alias(libs.plugins.compose.compiler) //should use compiler because i use kotlin 2.0

    alias(libs.plugins.kotlin.serialization)

}

android {
    namespace = "com.example.firebase_learn"
    compileSdk = 35 //update from 34 to 35

    defaultConfig {
        applicationId = "com.example.firebase_learn"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //viewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    //hilt &ksp
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //Hilt navigation
    implementation(libs.androidx.hilt.navigation.compose)


    //navigation
    // Jetpack Compose Navigation dependency
    implementation (libs.androidx.navigation.compose)
    //serizliztion
    implementation(libs.kotlinx.serialization.json)

    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.analytics)
    // Firebase Libraries
    implementation(libs.firebase.auth.ktx)       // Authentication
    implementation(libs.firebase.firestore.ktx)  // Firestore Database
    implementation(libs.firebase.analytics.ktx)  // Analytics
    implementation(libs.firebase.messaging.ktx)  // Cloud Messaging
    implementation(libs.firebase.storage.ktx)    // Cloud Storage

    //compose icons
    implementation ("androidx.compose.material:material-icons-extended:1.7.5")

}