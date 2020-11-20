package io.wax911.trakt.core.initializer

import android.content.Context
import android.util.Log
import io.wax911.trakt.core.initializer.contract.AbstractInitializer
import fr.bipi.tressence.file.FileLoggerTree
import io.wax911.trakt.core.BuildConfig
import timber.log.Timber
import java.io.File
import java.io.IOException

class TimberInitializer : AbstractInitializer<Unit>() {

    private fun getLogsCache(context: Context): File {
        val cache = context.externalCacheDir ?: context.cacheDir
        val logsName = "logs"
        val logs = File(cache, logsName)
        if (!logs.exists()) logs.mkdirs()
        return logs
    }

    @Throws(IOException::class)
    private fun createFileLoggingTree(context: Context): Timber.Tree {
        val loggingFile = getLogsCache(context)
        return FileLoggerTree.Builder()
            .withFileName("${context.packageName}.log")
            .withDirName(loggingFile.absolutePath)
            .withSizeLimit(logSizeLimit)
            .withFileLimit(1)
            .withMinPriority(logLevel)
            .appendToFile(true)
            .build()
    }

    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        val fileLoggerTree = createFileLoggingTree(context)
        Timber.plant(fileLoggerTree)
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())
    }

    companion object {
        val logLevel = if (BuildConfig.DEBUG) Log.VERBOSE else Log.WARN
        const val logSizeLimit = 850 * 1024
    }
}