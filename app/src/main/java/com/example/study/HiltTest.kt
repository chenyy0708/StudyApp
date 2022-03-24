package com.example.study

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


/**
 * 描述:
 * 作者: ChenYy
 * 日期: 2022/3/24 20:02
 */
interface AnalyticsService {
    fun analyticsMethods()
}

class AnalyticsServiceImpl @Inject constructor(
) : AnalyticsService {
    override fun analyticsMethods() {
        println("analyticsMethodsfwefwefwefwefwe")
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