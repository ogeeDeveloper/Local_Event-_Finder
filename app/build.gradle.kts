plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
//    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
//    id("com.google.gms.google-services")  // Add Google Services plugin
}

android {
    namespace = "com.ogeedeveloper.local_event_finder_frontend"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ogeedeveloper.local_event_finder_frontend"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // Read the Maps API key from gradle.properties
        val mapsApiKey = project.properties["MAPS_API_KEY"] as? String ?: "API_KEY_NOT_FOUND"
        
        // Add the API key as a BuildConfig field
        buildConfigField("String", "MAPS_API_KEY", "\"$mapsApiKey\"")
        
        // Also add it as a resource for XML files
        resValue("string", "google_maps_key", mapsApiKey)
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
        buildConfig = true  // Enable BuildConfig generation
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
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.github.skydoves:landscapist-glide:2.4.7")
    
    // Material Design components
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.material:material-icons-extended")
    
    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation("androidx.datastore:datastore-preferences:1.1.4")
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.5.0")
    
    // Google Maps
    implementation("com.google.maps.android:maps-compose:2.11.4")
    implementation("com.google.android.gms:play-services-maps:19.2.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    
    // Google Places API - use a stable version
    implementation("com.google.android.libraries.places:places:3.2.0")
    
    // Firebase Authentication
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    
    // Retrofit for network calls
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.12.0") // Add this for JavaNetCookieJar
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Pull-to-refresh
    implementation("com.google.accompanist:accompanist-swiperefresh:0.27.0")

    // Dagger Hilt dependencies
    implementation("com.google.dagger:dagger:2.51.1")
    implementation("com.google.dagger:hilt-android:2.51.1")
//    ksp("com.google.dagger:hilt-android-compiler:2.51.1") // Required for Hilt annotation processing
    kapt("com.google.dagger:hilt-android-compiler:2.56.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}