package com.amarchaud.data.di

import android.content.Context
import android.content.SharedPreferences
import com.amarchaud.data.settings.SettingsRepositoryImpl
import com.amarchaud.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Singleton
    @Binds
    abstract fun bindSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository

    companion object {
        @Singleton
        @Provides
        fun provideSettingsPreferences(@ApplicationContext context: Context): SharedPreferences =
            context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }
}
