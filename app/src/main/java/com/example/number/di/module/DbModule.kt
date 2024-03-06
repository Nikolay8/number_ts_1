package com.example.number.di.module

import android.app.Application
import androidx.room.Room
import com.example.number.dao.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Provides
    @Singleton
    fun provideRoomDatabaseRepository(
        application: Application
    ): AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java, DATABASE_NAME
    ).build()

    companion object {
        private const val DATABASE_NAME = "database_numbers_info.db"
    }
}
