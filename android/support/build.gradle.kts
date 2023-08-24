plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.teamtripdraw.android.support"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            buildConfigField("Boolean", "IS_RELEASE", "true")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        getByName("debug") {
            buildConfigField("Boolean", "IS_RELEASE", "false")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    dataBinding {
        enable = true
    }
    viewBinding {
        enable = true
    }
}

dependencies {
    // 안드로이드 기본 의존성
    implementation(libs.bundles.androidDefault)

    // 테스트 의존성
    testImplementation(libs.bundles.androidUnitTest)

    // HttpClient 관련 의존성
    implementation(libs.bundles.httpClient)

    // Glide
    implementation(libs.glide)

    // NaverMap
    implementation(libs.bundles.naverMap)

    // kakaoLogin
    implementation(libs.kakaoUser)

    // sentry
    implementation(libs.sentry)
}
