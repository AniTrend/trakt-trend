package io.wax911.trakt

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import fr.bipi.tressence.file.FileLoggerTree
import io.wax911.trakt.core.TraktTrendApplication
import io.wax911.trakt.koin.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber
import java.io.File

class App : TraktTrendApplication() {

    private fun createUncaughtExceptionHandler() {
        val exceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Timber.tag(t.name).e(e)
            exceptionHandler?.uncaughtException(t, e)
        }
    }

    private fun getLogsCache(): File {
        val cache = externalCacheDir ?: cacheDir
        val logsName = "logs"
        val logs = File(cache, logsName)
        if (!logs.exists()) logs.mkdirs()
        Timber.tag(javaClass.simpleName).v(
            "Directory that will be used for logs: ${logs.canonicalPath}"
        )
        return logs
    }

    /** [Koin](https://insert-koin.io/docs/2.0/getting-started/)
     *
     * Initializes dependencies for the entire application, this function is automatically called
     * in [onCreate] as the first call to assure all injections are available
     */
    override fun initializeDependencyInjection() {
        startKoin {
            fragmentFactory()
            androidLogger(
                level = if (BuildConfig.DEBUG) Level.DEBUG else Level.ERROR
            )
            androidContext(
                applicationContext
            )
            modules(appModules)
        }
    }

    /**
     * Timber logging tree depending on the build type we plant the appropriate tree
     */
    override fun plantLoggingTree() {
        super.plantLoggingTree()
        val logLevel = if (BuildConfig.DEBUG) Log.VERBOSE else Log.WARN
        val logSizeLimit = 750 * 1024
        val fileLogger = FileLoggerTree.Builder()
            .withFileName("$packageName.log")
            .withDirName(getLogsCache().absolutePath)
            .withSizeLimit(logSizeLimit)
            .withFileLimit(1)
            .withMinPriority(logLevel)
            .appendToFile(true)
            .build()
        Timber.plant(fileLogger)
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
