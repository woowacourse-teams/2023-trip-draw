import java.util.Properties

val localProperties = Properties()
localProperties.load(project.rootProject.file("local.properties").inputStream())
val keystoreProperties = Properties()
keystoreProperties.load(project.rootProject.file("keystore.properties").inputStream())

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    // FireBase
    id("com.google.gms.google-services")
    // FireBaseCrashlytics
    id("com.google.firebase.crashlytics")
}

android {
    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }
    namespace = "com.teamtripdraw.android"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.teamtripdraw.android"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.appVersion.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            localProperties.getProperty("KAKAO_NATIVE_APP_KEY"),
        )

        buildConfigField(
            "String",
            "ENCRYPTED_SHARED_PREFERENCE_MASTER_KEY_ALIAS",
            localProperties.getProperty("ENCRYPTED_SHARED_PREFERENCE_MASTER_KEY_ALIAS"),
        )

        manifestPlaceholders["NATIVE_APP_KEY"] =
            localProperties.getProperty("KAKAO_NATIVE_APP_KEY_NO_QUOTES")

        manifestPlaceholders["NAVER_MAP_CLIENT_ID"] =
            localProperties.getProperty("NAVER_MAP_CLIENT_ID")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            buildConfigField("Boolean", "IS_RELEASE", "true")
            manifestPlaceholders["appLabel"] = "TRIP DRAW"
            buildConfigField(
                "String",
                "TRIP_DRAW_BASE_URL",
                localProperties.getProperty("TRIP_DRAW_PROD_BASE_URL"),
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            buildConfigField("Boolean", "IS_RELEASE", "false")
            manifestPlaceholders["appLabel"] = "(Dev)TRIP DRAW"
            buildConfigField(
                "String",
                "TRIP_DRAW_BASE_URL",
                localProperties.getProperty("TRIP_DRAW_DEV_BASE_URL"),
            )
            applicationIdSuffix = ".debug"
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
    implementation(libs.bundles.naverMap)

    // FireBase
    implementation(platform(libs.fireBaseBom))
    // FireBaseAnalytics
    releaseImplementation(libs.fireBaseAnalyticsKtx)
    // FirebaseCrashlytics
    releaseImplementation(libs.fireBaseCrashlyticsKtx)

    // EncryptedSharedPreference
    implementation(libs.encryptedSharedPreference)

    // Stfalcon
    implementation(libs.stfalcon)

    // kakaoLogin
    implementation(libs.kakaoUser)

    // splashScreen
    implementation(libs.splashScreen)
}
