plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("WMRouter")
    id("Lifecycle")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Versions.COMPILE_SDK
    defaultConfig {
        applicationId = "com.example.study"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.versionCodeMobile
        versionName = Versions.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("bool", "android_god_eye_manual_install", "false") // 是否手动安装，默认false
            resValue(
                "bool",
                "android_god_eye_need_notification",
                "false"
            ) // 是否展示通知栏，默认false，生产环境请关闭
            resValue("integer", "android_god_eye_monitor_port", "5399") // Monitor端口，默认5390
            resValue(
                "string",
                "android_god_eye_install_assets_path",
                "android-godeye-config/release_install.config"
            ) // 安装配置文件在assets中的路径
        }
        getByName("debug") {
            resValue("bool", "android_god_eye_manual_install", "false")
            resValue("bool", "android_god_eye_need_notification", "true")
            resValue("integer", "android_god_eye_monitor_port", "5399")
            resValue(
                "string",
                "android_god_eye_install_assets_path",
                "android-godeye-config/install.config"
            )
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
//        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        val options = this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        options.jvmTarget = "1.8"
    }
}

dependencies {
    kapt(platform(project(":compiler")))
    implementation(project(":user"))
    implementation(project(":shop"))
    implementation(project(":home"))
    implementation(project(":common"))
    implementation(Libs.KOTLIN_STDLIB)
    implementation(Libs.CORE_KTX)
    implementation(Libs.APPCOMPAT)
    implementation(Libs.MATERIAL)
    implementation(Libs.CONSTRAINT_LAYOUT)
    implementation(Libs.JUNIT)
    implementation(Libs.ESPRESSO_CORE)
    implementation(Libs.EXT_JUNIT)
    implementation(Libs.GLIDE)
    kapt(Libs.GLIDE_COMPILER)
    implementation(Libs.RX_JAVA)
    implementation(Libs.RX_ANDROID)
    implementation(Libs.COROUTINES)
    implementation(Libs.COROUTINES_ANDROID)
    implementation(Libs.FRAGMENT_KTX)
    implementation(Libs.LIFECYCLE_RUNTIME_KTX)
    implementation(Libs.LIFECYCLE_LIVE_DATA_KTX)
    implementation(Libs.LIFECYCLE_VIEW_MODEL_KTX)
    implementation(Libs.DATA_STORE_PREFERENCES)
    implementation(Libs.DATA_STORE)
    implementation(Libs.APP_STARTUP)
    implementation(Libs.RETROFIT)
    implementation(Libs.LEAK_CANARY)
    implementation(Libs.GOD_EYE)
    kapt(Libs.WM_ROUTER_COMPILER)
    kapt(Libs.HILT_COMPILER)
    implementation(Libs.HILT)
}

