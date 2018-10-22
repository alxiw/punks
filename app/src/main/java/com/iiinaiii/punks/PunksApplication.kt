package com.iiinaiii.punks

import android.app.Application
import android.content.Context
import com.iiinaiii.punks.dagger.CoreComponent
import com.iiinaiii.punks.dagger.DaggerCoreComponent
import com.jakewharton.threetenabp.AndroidThreeTen

class PunksApplication : Application() {

    lateinit var coreComponent: CoreComponent

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        coreComponent = DaggerCoreComponent.builder().build()
    }

    companion object {
        fun component(context: Context): CoreComponent =
            (context.applicationContext as PunksApplication).coreComponent
    }
}
