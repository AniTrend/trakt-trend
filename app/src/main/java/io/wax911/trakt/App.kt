package io.wax911.trakt

import androidx.appcompat.app.AppCompatDelegate
import io.wax911.trakt.core.TraktTrendApplication
import timber.log.Timber

class App : TraktTrendApplication() {

    private fun createUncaughtExceptionHandler() {
        val exceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Timber.tag(t.name).e(e)
            exceptionHandler?.uncaughtException(t, e)
        }
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(
            AppCompatDelegate.MODE_NIGHT_NO
        )
        if (BuildConfig.DEBUG)
            createUncaughtExceptionHandler()
    }
}
