plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("WMRouter")
    id("Lifecycle")
    id("dagger.hilt.android.plugin")
    id("AGPPlugin")
    id("com.tencent.matrix-plugin")
}

matrix {
    trace {
        isEnable = true
        baseMethodMapFile = "${project.buildDir}/matrix_output/Debug.methodmap"
        blackListFile = "${project.projectDir}/matrixTrace/blackMethodList.txt"
    }
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
        }
        getByName("debug") {
        }
    }

    buildFeatures {
        viewBinding = true
//        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.COMPOSE_VERSION
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        val options = this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        options.jvmTarget = "1.8"
    }

    packagingOptions {
        jniLibs.pickFirsts.add("lib/armeabi-v7a/libc++_shared.so")
        jniLibs.pickFirsts.add("lib/arm64-v8a/libc++_shared.so")
        jniLibs.pickFirsts.add("lib/armeabi-v7a/libwechatbacktrace.so")
        jniLibs.pickFirsts.add("lib/arm64-v8a/libwechatbacktrace.so")
    }
}

configurations {
    debugImplementation.get().exclude(group = "junit",module = "junit")
}

dependencies {
    kapt(platform(project(":compiler")))
    kaptList(Libs.compiler)
    implementation(project(":user"))
    implementation(project(":shop"))
    implementation(project(":home"))
    implementation(project(":common"))
    implementationList(Libs.kotlin)
    implementationList(Libs.androidx)
    implementationList(Libs.test)
    implementationList(Libs.compose)
    implementationList(Libs.coil)
    implementationList(Libs.extend)
    implementationList(Libs.rxjava)
    implementationList(Libs.hilt)
    implementationList(Libs.matrix)

    debugImplementation("androidx.compose.ui:ui-tooling-preview:${Versions.COMPOSE_VERSION}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.COMPOSE_VERSION}")
}

