/*
 * Copyright 2020 Google LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

object Libs {
    val androidx = listOf(
        "androidx.constraintlayout:constraintlayout:2.0.4",
        "com.google.android.material:material:1.3.0",
        "androidx.appcompat:appcompat:1.3.0",
    )
    val test = listOf(
        "androidx.test.ext:junit:1.1.2",
        "junit:junit:4.13.2",
        "androidx.test.espresso:espresso-core:3.3.0",

        )
    val kotlin = listOf(
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINE}",
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINE}",
        "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"
    )

    val ktx = listOf(
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}",
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}",
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE}",
        "androidx.fragment:fragment-ktx:1.3.4",
        "androidx.core:core-ktx:1.5.0"
    )
    val jetPack = listOf(
        "androidx.startup:startup-runtime:1.0.0",
        "androidx.datastore:datastore:1.0.0-beta01",
        "androidx.datastore:datastore-preferences:1.0.0-beta01"
    )
    val square = listOf(
        "com.squareup.retrofit2:retrofit:2.9.0",
        "com.squareup.leakcanary:leakcanary-android:2.7",
    )
    val rxjava = listOf(
        "io.reactivex.rxjava2:rxjava:2.2.21",
        "io.reactivex.rxjava2:rxandroid:2.1.1"
    )
    val compiler = listOf(
        "io.github.meituan-dianping:compiler:1.2.1",
        "com.github.bumptech.glide:compiler:${Versions.GLIDE}",
        "com.google.dagger:hilt-android-compiler:2.36",
        "androidx.hilt:hilt-compiler:1.0.0-alpha01",
    )
    val router = listOf(
        "com.alibaba:arouter-api:1.5.1",
        "io.github.meituan-dianping:router:1.2.1"
    )
    val extend = listOf(
        "com.hi-dhl:binding:1.1.3",
        "androidx.tracing:tracing:1.0.0",
        "androidx.tracing:tracing-ktx:1.0.0",
        "cn.hikyson.godeye:godeye-core:3.4.3",
        "com.github.bumptech.glide:glide:${Versions.GLIDE}",
    )
    val hilt = listOf(
        "com.google.dagger:hilt-android:2.36",
        "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha01"
    )
    val compose = listOf(
        "androidx.compose.runtime:runtime:${Versions.COMPOSE_VERSION}",
        "androidx.compose.ui:ui:${Versions.COMPOSE_VERSION}",
        "androidx.compose.foundation:foundation-layout:${Versions.COMPOSE_VERSION}",
        "androidx.compose.material:material:${Versions.COMPOSE_VERSION}",
        "androidx.compose.material:material-icons-extended:${Versions.COMPOSE_VERSION}",
        "androidx.compose.foundation:foundation:${Versions.COMPOSE_VERSION}",
        "androidx.compose.animation:animation:${Versions.COMPOSE_VERSION}",
        "androidx.compose.ui:ui-tooling-preview:${Versions.COMPOSE_VERSION}",
        "androidx.compose.runtime:runtime-livedata:${Versions.COMPOSE_VERSION}",
        "androidx.compose.ui:ui-tooling:${Versions.COMPOSE_VERSION}",
        "androidx.constraintlayout:constraintlayout-compose:1.0.0",
        "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07",
        "androidx.activity:activity-compose:1.3.1",
        "com.google.accompanist:accompanist-pager:0.24.6-alpha",
    )
    val coil = listOf(
        "io.coil-kt:coil:2.0.0-rc03",
        "io.coil-kt:coil-compose:2.0.0-rc03"
    )
    const val A_ROUTER = "com.alibaba:arouter-api:1.5.1"
    const val HILT_PLUGIN = "com.google.dagger:hilt-android-gradle-plugin:2.36"
    const val A_ROUTER_COMPILER = "com.alibaba:arouter-compiler:1.2.2"

    const val MATRIX_PLUGIN = "com.tencent.matrix:matrix-gradle-plugin:${Versions.MATRIX_VERSION}"

    val matrix = listOf(
        "com.tencent.matrix:matrix-android-lib:${Versions.MATRIX_VERSION}",
        "com.tencent.matrix:matrix-android-commons:${Versions.MATRIX_VERSION}",
        "com.tencent.matrix:matrix-trace-canary:${Versions.MATRIX_VERSION}",
        "com.tencent.matrix:matrix-resource-canary-android:${Versions.MATRIX_VERSION}",
        "com.tencent.matrix:matrix-resource-canary-common:${Versions.MATRIX_VERSION}",
        "com.tencent.matrix:matrix-io-canary:${Versions.MATRIX_VERSION}",
        "com.tencent.matrix:matrix-sqlite-lint-android-sdk:${Versions.MATRIX_VERSION}",
        "com.tencent.matrix:matrix-battery-canary:${Versions.MATRIX_VERSION}",
        "com.tencent.matrix:matrix-hooks:${Versions.MATRIX_VERSION}",
    )
}
