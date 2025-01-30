import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.ricky.theorify"
    compileSdk = 35

    // Para ter acesso ao conteúdo do ficheiro local.properties
    val file = rootProject.file("local.properties")
    val properties = Properties()
    // Carrega o conteúdo para dentro do properties
    properties.load(FileInputStream(file))

    defaultConfig {
        applicationId = "com.ricky.theorify"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildFeatures.buildConfig = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Estabelece a criação da chave no programa
        buildConfigField("String", "bearer", properties.getProperty("bearer"))
    }

    buildTypes {
        debug {
            // Estabelece a criação da chave no programa
            buildConfigField("String", "bearer", properties.getProperty("bearer"))
        }
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Implementação do Retrofit para acesso à API
    implementation ("com.squareup.retrofit2:retrofit:2.3.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.3.0")
}