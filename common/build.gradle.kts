plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}
apply {
    from("$rootDir/config/module_build_config.gradle")
    from("$rootDir/config/dependencies.gradle")
    from("lorem.gradle.kts")
}

dependencies {
    api(Libs.A_ROUTER)
    api(Libs.WM_ROUTER)
    api(Libs.BINDING)

    api(project(":service"))
    api(project(":launcher"))
    api(project(":interfaces"))
    api(project(":moduleLike"))
}