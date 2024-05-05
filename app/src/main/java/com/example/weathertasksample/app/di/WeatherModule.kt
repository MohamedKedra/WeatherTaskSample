package com.example.weathertasksample.app.di

import android.content.Context
import androidx.room.Room
import com.example.weathertasksample.app.utils.ConnectionManager
import com.example.weathertasksample.app.utils.Constant
import com.example.weathertasksample.data.local.WeatherDao
import com.example.weathertasksample.data.local.WeatherDatabase
import com.example.weathertasksample.data.remote.WeatherService
import com.example.weathertasksample.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WeatherModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, WeatherDatabase::class.java, Constant.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @Singleton
    @Provides
    fun provideDao(weatherDatabase: WeatherDatabase) = weatherDatabase.weatherDao()

    @Singleton
    @Provides
    fun provideRepository(dao: WeatherDao, service: WeatherService) =
        WeatherRepository(service, dao)

    @Singleton
    @Provides
    fun provideNetwork(@ApplicationContext context: Context) =
        ConnectionManager(context)

    @Singleton
    @Provides
    fun providesRetrofit() = Retrofit.Builder()
        .baseUrl(Constant.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideService(retrofit: Retrofit) = retrofit.create(WeatherService::class.java)
}