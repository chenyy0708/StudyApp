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
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib:" + Versions.KOTLIN
    const val CORE_KTX = "androidx.core:core-ktx:1.5.0"
    const val APPCOMPAT = "androidx.appcompat:appcompat:1.3.0"
    const val MATERIAL = "com.google.android.material:material:1.3.0"
    const val CONSTRAINT_LAYOUT = "androidx.constraintlayout:constraintlayout:2.0.4"

    const val JUNIT = "junit:junit:4.13.2"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:3.3.0"
    const val EXT_JUNIT = "androidx.test.ext:junit:1.1.2"
    const val GLIDE = "com.github.bumptech.glide:glide:" + Versions.GLIDE
    const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:" + Versions.GLIDE
    const val RX_JAVA = "io.reactivex.rxjava2:rxjava:2.2.21"
    const val RX_ANDROID = "io.reactivex.rxjava2:rxandroid:2.1.1"
    const val COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-core:" + Versions.COROUTINE
    const val COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:" + Versions.COROUTINE
    const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:1.3.4"
    const val LIFECYCLE_RUNTIME_KTX =
        "androidx.lifecycle:lifecycle-runtime-ktx:" + Versions.LIFECYCLE
    const val LIFECYCLE_LIVE_DATA_KTX =
        "androidx.lifecycle:lifecycle-livedata-ktx:" + Versions.LIFECYCLE
    const val LIFECYCLE_VIEW_MODEL_KTX =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:" + Versions.LIFECYCLE
    const val DATA_STORE_PREFERENCES = "androidx.datastore:datastore-preferences:1.0.0-beta01"
    const val DATA_STORE = "androidx.datastore:datastore:1.0.0-beta01"
    const val APP_STARTUP = "androidx.startup:startup-runtime:1.0.0"
    const val RETROFIT = "com.squareup.retrofit2:retrofit:2.9.0"
    const val LEAK_CANARY = "com.squareup.leakcanary:leakcanary-android:2.7"
    const val GOD_EYE = "cn.hikyson.godeye:godeye-core:3.4.3"
    const val WM_ROUTER_COMPILER = "io.github.meituan-dianping:compiler:1.2.1"
    const val WM_ROUTER = "io.github.meituan-dianping:router:1.2.1"
    const val A_ROUTER_COMPILER = "com.alibaba:arouter-compiler:1.2.2"
    const val A_ROUTER = "com.alibaba:arouter-api:1.5.1"
    const val BINDING = "com.hi-dhl:binding:1.1.3"
    const val HILT_ANDROID = "com.google.dagger:hilt-android:2.36"
    const val HILT_ANDROID_COMPILER = "com.google.dagger:hilt-android-compiler:2.36"
    const val HILT_PLUGIN = "com.google.dagger:hilt-android-gradle-plugin:2.36"
    const val HILT_COMPILER = "androidx.hilt:hilt-compiler:1.0.0-alpha01"
    const val HILT_VIEW_MODEL = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha01"

    const val TRACING = "androidx.tracing:tracing:1.0.0"
    const val TRACING_KTX = "androidx.tracing:tracing-ktx:1.0.0"

}
