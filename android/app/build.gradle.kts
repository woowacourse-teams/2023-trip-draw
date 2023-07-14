import org.jetbrains.kotlin.konan.properties.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.teamtripdraw.android"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.teamtripdraw.android"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.appVersion.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        manifestPlaceholders["NAVER_MAP_CLIENT_ID"] =
            properties.getProperty("NAVER_MAP_CLIENT_ID")
    }

    buildTypes {
        getByName("release") {
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
    dataBinding {
        enable = true
    }
    viewBinding {
        enable = true
    }
    // mockk 충돌문제
    packagingOptions {
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE-notice.md")
    }
    testOptions {
        packagingOptions {
            jniLibs {
                useLegacyPackaging = true
            }
        }
    }
}

dependencies {
    // 프로젝트내 의존성
    implementation(project(":domain"))
    implementation(project(":support"))

    // 안드로이드 기본 의존성
    implementation(libs.bundles.androidDefault)

    // 테스트 의존성
    testImplementation(libs.bundles.androidUnitTest)

    // 안드로이드 테스트 의존성
    androidTestImplementation(libs.bundles.androidTest)

    // HttpClient 관련 의존성
    implementation(libs.bundles.httpClient)

    // Glide
    implementation(libs.glide)

    // NaverMap
    implementation(libs.naverMap)
}
