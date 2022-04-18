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
    apiList(Libs.router)
    apiList(Libs.extend)
    apiList(Libs.square)
    apiList(Libs.jetPack)
    apiList(Libs.ktx)
    api(project(":service"))
    api(project(":launcher"))
    api(project(":interfaces"))
    api(project(":moduleLike"))
}