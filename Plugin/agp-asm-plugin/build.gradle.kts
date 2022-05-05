plugins {
    id("java-gradle-plugin")
    kotlin("jvm")
    id("maven-publish")
}

repositories {
    google()
    jcenter()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.example.study"
            artifactId = "agp-asm-plugin"
            version = "1.0.1"
            // 组件类型，我们的插件其实就是Java组件
            from(components["java"])
        }
    }

    repositories {
        maven {
            maven(url = "${project.rootDir}/Plugin/plugins")
        }
    }
}

//tasks.withType<KotlinCompile>().configureEach {
//    kotlinOptions.apiVersion = "1.3"
//}

dependencies {
    implementation("com.android.tools.build:gradle-api:7.0.3")
    implementation("com.android.tools.build:gradle:7.0.3")
    implementation(kotlin("stdlib"))
    implementation(gradleApi())
}
dependencies {
    implementation("org.ow2.asm:asm-util:7.0")
}