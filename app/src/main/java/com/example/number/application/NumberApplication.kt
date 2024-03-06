package com.example.number.application

import android.app.Application
import com.example.number.dao.AppDatabase
import com.example.number.data.repository.NetworkRepository
import com.example.number.di.component.ApplicationComponent
import com.example.number.di.component.DaggerApplicationComponent
import com.example.number.di.module.ApplicationModule
import com.example.number.di.module.DbModule
import com.example.number.di.module.NetworkModule
import javax.inject.Inject

class NumberApplication : Application() {

    companion object {
        private const val TAG = "NumberApplication"

        @JvmStatic
        lateinit var applicationComponent: ApplicationComponent
    }

    @Inject
    lateinit var networkRepository: NetworkRepository

    @Inject
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        // Inject applicationComponent with modules
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(
            ApplicationModule(this)
        ).networkModule(NetworkModule()).dbModule(DbModule()).build()
        applicationComponent.inject(this)

    }
}