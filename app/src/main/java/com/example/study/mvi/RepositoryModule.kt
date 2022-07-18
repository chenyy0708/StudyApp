package com.example.study.mvi

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

/**
 * Created by chenyy on 2022/7/18.
 */

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    fun provideMainRepository() = MainRepository()
}