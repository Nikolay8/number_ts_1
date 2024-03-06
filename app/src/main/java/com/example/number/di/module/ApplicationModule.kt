package com.example.number.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule constructor(private val application: Application) {

    @Provides
    fun provideApplication(): Application = application

    @Provides
    fun provideApplicationContext(): Context = application
}
