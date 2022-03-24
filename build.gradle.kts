buildscript {

    repositories {
        google()
        mavenCentral()
        maven(url = "${project.rootDir}/Plugin/plugins")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("io.github.meituan-dianping:plugin:1.2.1")
        classpath("com.example.study:lifecycle-plugin:1.3.0")
        classpath(Libs.HILT_PLUGIN)
    }
}

plugins {
    `kotlin-dsl`
    id("com.diffplug.gradle.spotless") version "3.27.1"
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.aliyun.com/repository/public")
    }
}

