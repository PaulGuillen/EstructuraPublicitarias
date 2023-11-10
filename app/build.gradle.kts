plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
}

android {
    namespace = "com.devpaul.estructurapublicitarias_roal"
    compileSdk = 33
    flavorDimensions("environment")

    defaultConfig {
        applicationId = "com.devpaul.estructurapublicitarias_roal"
        minSdk = 26
        targetSdk = 33
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
    }

    productFlavors {
        create("prod") {
            dimension = "environment"
            applicationId = "com.devpaul.estructurapublicitarias_roal.prod"
            buildConfigField("String", "BASE_URL", "\"https://yam7m0qth8.execute-api.us-east-2.amazonaws.com/prod/\"")
            versionCode = 1
            versionName = "1.0"
        }

        create("cert") {
            dimension = "environment"
            applicationId = "com.devpaul.estructurapublicitarias_roal.cert"
            buildConfigField("String", "BASE_URL", "\"https://yam7m0qth8.execute-api.us-east-2.amazonaws.com/cert/\"")
            versionCode = 1000
            versionName = "1.1"
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding.isEnabled = true
    dataBinding.isEnabled = true
}

dependencies {

    //  Keep on 1.9.0 , api limit
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //  Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    //  Gson
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //  Glide
    implementation("com.github.bumptech.glide:glide:4.14.2")

    //  Image Picker
    implementation("com.github.dhaval2404:imagepicker:2.1")

    //  Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    //CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //  Dialog library
    implementation("com.github.f0ris.sweetalert:library:1.6.2")

    //  Timber = Logs
    implementation("com.jakewharton.timber:timber:4.7.1")

    //  Scan bar code
    implementation("com.journeyapps:zxing-android-embedded:4.3.0") {
        isTransitive = false
    }
    implementation("com.google.zxing:core:3.4.1")

    //  Facebook animation loading
    implementation("com.facebook.shimmer:shimmer:0.1.0@aar")

    // PlayCore appUpdate Google Play
    implementation("com.google.android.play:core:1.10.3")

    val lifeCycleVersion = "2.2.0"
    dependencies {
        add("implementation", "androidx.lifecycle:lifecycle-extensions:$lifeCycleVersion")
        add("implementation", "androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycleVersion")
        add("implementation", "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVersion")
        add("implementation", "androidx.lifecycle:lifecycle-livedata-ktx:$lifeCycleVersion")
    }

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}