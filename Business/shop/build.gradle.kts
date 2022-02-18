plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}
apply {
    from("$rootDir/config/module_build_config.gradle")
    from("$rootDir/config/dependencies.gradle")
}

android {
    defaultConfig {
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
    }
}

dependencies {
    kapt(Libs.A_ROUTER_COMPILER)
    kapt(platform(project(":compiler")))
    implementation(project(":common"))
}