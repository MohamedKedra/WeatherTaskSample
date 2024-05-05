package com.example.weathertasksample.app.di

import android.content.Context
import androidx.room.Room
import com.example.weathertasksample.app.utils.ConnectionManager
import com.example.weathertasksample.app.utils.Constant
import com.example.weathertasksample.data.local.WeatherDao
import com.example.weathertasksample.data.local.WeatherDatabase
import com.example.weathertasksample.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WeatherModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, WeatherDatabase::class.java, Constant.DATABASE_NAME)
            .allowMainThreadQueries()
            .build()

    @Singleton
    @Provides
    fun provideDao(weatherDatabase: WeatherDatabase) = weatherDatabase.weatherDao()

    @Singleton
    @Provides
    fun provideRepository(dao: WeatherDao) = WeatherRepository(dao)

    @Singleton
    @Provides
    fun provideNetwork(@ApplicationContext context: Context) =
        ConnectionManager(context)
}