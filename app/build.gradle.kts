plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.number"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.number"
        minSdk = 26
        //noinspection OldTargetApi
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    val roomVer = "2.6.1"
    val coreKtxVer = "1.12.0"
    val appcompatVer = "1.6.1"
    val materialVer = "1.11.0"
    val constraintlayoutVer = "2.1.4"
    val lifecycleExtensionsVer = "2.2.0"
    val lifecycleViewmodelSavedstateVer = "2.7.0"
    val fragmentKtxVer = "1.6.2"
    val symbolProcessingApiVer = "1.9.21-1.0.15"
    val databindingRuntimVer = "8.3.0"
    val junitVer = "4.13.2"
    val extJunitVer = "1.1.5"
    val espressoCoreVer = "3.5.1"
    val chuckerVer = "4.0.0"
    val daggerVer = "2.47"
    val retrofitVer = "2.9.0"
    val converterMoshiVer = "2.9.0"
    val moshiVer = "1.15.0"



    implementation("androidx.core:core-ktx:$coreKtxVer")
    implementation("androidx.appcompat:appcompat:$appcompatVer")
    implementation("com.google.android.material:material:$materialVer")
    implementation("androidx.constraintlayout:constraintlayout:$constraintlayoutVer")
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleExtensionsVer")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleViewmodelSavedstateVer")
    implementation("androidx.fragment:fragment-ktx:$fragmentKtxVer")
    implementation("com.google.devtools.ksp:symbol-processing-api:$symbolProcessingApiVer")
    implementation("androidx.databinding:databinding-runtime:$databindingRuntimVer")
    testImplementation("junit:junit:$junitVer")
    androidTestImplementation("androidx.test.ext:junit:$extJunitVer")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoCoreVer")

    //Chuck HTTP inspector
    debugImplementation("com.github.chuckerteam.chucker:library:$chuckerVer")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:$chuckerVer")

    //Dagger
    implementation("com.google.dagger:dagger:$daggerVer")
    kapt("com.google.dagger:dagger-compiler:$daggerVer")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVer")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVer")

    // Room
    implementation("androidx.room:room-runtime:$roomVer")
    annotationProcessor("androidx.room:room-compiler:$roomVer")
    // To use Kotlin annotation processing tool (kapt)
    //noinspection KaptUsageInsteadOfKsp
    kapt("androidx.room:room-compiler:$roomVer")

    //Moshi
    implementation("com.squareup.retrofit2:converter-moshi:$converterMoshiVer")
    implementation("com.squareup.moshi:moshi:$moshiVer")
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVer")
    implementation("com.squareup.moshi:moshi-adapters:$moshiVer")
    //noinspection KaptUsageInsteadOfKsp
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshiVer")
}