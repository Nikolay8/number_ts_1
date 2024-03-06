package com.example.number.di.component

import com.example.number.application.NumberApplication
import com.example.number.di.module.ApplicationModule
import com.example.number.di.module.DbModule
import com.example.number.di.module.NetworkModule
import com.example.number.ui.NumberFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        NetworkModule::class,
        DbModule::class
    ]
)
interface ApplicationComponent {

    fun inject(numberApplication: NumberApplication)

    fun inject(numberFragment: NumberFragment)
}
