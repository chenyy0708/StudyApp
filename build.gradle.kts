import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {

    repositories {
        google()
        mavenCentral()
        jcenter()
        // Android Build Server
        maven {
            url = uri("${project.rootDir}/Plugin/plugins")
        }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("io.github.meituan-dianping:plugin:1.2.1")
        classpath("com.example.study:lifecycle-plugin:1.3.0")
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
        maven { url = uri("https://maven.aliyun.com/repository/public") }
    }
}

subprojects {
    apply(plugin = "com.diffplug.gradle.spotless")
    val ktlintVer = "0.40.0"
    spotless {
        kotlin {
            target("**/*.kt")
            ktlint(ktlintVer).userData(
                mapOf("max_line_length" to "100", "disabled_rules" to "import-ordering")
            )
            licenseHeaderFile(project.rootProject.file("copyright.kt"))
        }
        kotlinGradle {
            // same as kotlin, but for .gradle.kts files (defaults to '*.gradle.kts')
            target("**/*.gradle.kts")
            ktlint(ktlintVer)
            licenseHeaderFile(project.rootProject.file("copyright.kt"), "(plugins |import |include)")
        }
    }

    tasks.whenTaskAdded {
        if (name == "preBuild") {
            mustRunAfter("spotlessCheck")
        }
    }
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs +=
            "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi"
    }
}
