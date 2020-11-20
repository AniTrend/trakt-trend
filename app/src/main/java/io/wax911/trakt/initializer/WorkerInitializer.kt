package io.wax911.trakt.initializer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.WorkManager
import io.wax911.trakt.BuildConfig
import io.wax911.trakt.core.initializer.KoinInitializer
import io.wax911.trakt.core.initializer.contract.AbstractInitializer

class WorkerInitializer : AbstractInitializer<WorkManager>() {

    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context): WorkManager {
        val logLevel = when (BuildConfig.DEBUG) {
            true -> Log.VERBOSE
            else -> Log.ERROR
        }

        val configuration = Configuration.Builder()
            .setMinimumLoggingLevel(logLevel)
            .build()
        WorkManager.initialize(context, configuration)
        return WorkManager.getInstance(context)
    }

    /**
     * @return A list of dependencies that this [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     *
     * For e.g. if a [Initializer] `B` defines another [Initializer] `A` as its dependency,
     * then `A` gets initialized before `B`.
     */
    override fun dependencies() =
        listOf(KoinInitializer::class.java)
}