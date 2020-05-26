package com.hugomatilla.moviesflow

import android.app.Application
import com.hugomatilla.moviesflow.data.db.AppDB
import com.hugomatilla.moviesflow.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MoviesFlowApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppDB.init(applicationContext)
        startKoin {
            androidLogger()
            androidContext(this@MoviesFlowApplication)
            modules(appModule)
        }
    }
}