package com.example.study

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton


/**
 * 描述:
 * 作者: ChenYy
 * 日期: 2022/3/24 20:02
 */

@Singleton
class User @Inject constructor(/*val analyticsServiceImpl: AnalyticsServiceImpl*/) {
    val age: String = "18"
    val name: String = "he"
    fun test() {
        logD("age:$age--name:$name")
//        logD("test:${analyticsServiceImpl.analyticsMethods()}")
    }
}

interface AnalyticsService {
    fun analyticsMethods()
}

class AnalyticsServiceImpl @Inject constructor(
    user: User
) : AnalyticsService {
    override fun analyticsMethods() {
        logD("analyticsMethods:${this}")
    }
}

@Module
@InstallIn(ActivityComponent::class)
abstract class AnalyticsModule {

    @Binds
    abstract fun bindAnalyticsService(
        analyticsServiceImpl: AnalyticsServiceImpl
    ): AnalyticsService

}

@Module
@InstallIn(ActivityComponent::class)
object RetrofitModule {
    @Provides
    fun providerRetrofit(): Retrofit =
        Retrofit.Builder().baseUrl("https://www.baodu.com").build()
}
