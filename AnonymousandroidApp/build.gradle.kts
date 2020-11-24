plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
    id("kotlin-android")
}
group = "com.example.anonymouschat"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
}
dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("com.google.firebase:firebase-auth:20.0.1")
    implementation("com.google.firebase:firebase-database:19.5.1")
    implementation("com.google.firebase:firebase-firestore:22.0.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlin_version"]}")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")

    implementation ("android.arch.lifecycle:extensions:1.1.1")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation ("io.reactivex.rxjava3:rxjava:3.0.7")

    implementation ("com.squareup.retrofit2:retrofit:2.5.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.5.0")


}
android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.example.anonymouschat.AnonymousandroidApp"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        viewBinding = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}