package com.rustan.sonerimtest

import android.app.Application
import com.rustan.data.di.networkModule
import com.rustan.detail.di.detailFeatureModule
import com.rustan.domain.di.domainModule
import com.rustan.home.di.homeFeatureModule
import com.rustan.sonerimtest.di.myAppModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(
                myAppModules,
                networkModule,
                domainModule,
                homeFeatureModule,
                detailFeatureModule
            )
        }
    }
}