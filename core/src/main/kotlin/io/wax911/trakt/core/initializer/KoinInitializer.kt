package io.wax911.trakt.core.initializer

import android.content.Context
import androidx.startup.Initializer
import io.wax911.trakt.core.BuildConfig
import io.wax911.trakt.core.initializer.contract.AbstractInitializer
import io.wax911.trakt.core.koin.coreModules
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.KOIN_TAG
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import timber.log.Timber

class KoinInitializer : AbstractInitializer<Unit>() {

    /**
     * Initializes and a component given the application [Context]
     *
     * @param context The application context.
     */
    override fun create(context: Context) {
        val logLevel = if (BuildConfig.DEBUG) Level.DEBUG else Level.ERROR
        startKoin {
            androidContext(context)
            fragmentFactory()
            logger(KoinLogger(logLevel))
            modules(coreModules)
        }
    }

    /**
     * @return A list of dependencies that this [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     *
     * For e.g. if a [Initializer] `B` defines another
     * [Initializer] `A` as its dependency, then `A` gets initialized before `B`.
     */
    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(TimberInitializer::class.java)

    internal class KoinLogger(
        logLevel: Level
    ) : Logger(logLevel) {
        override fun log(level: Level, msg: MESSAGE) {
            when (level) {
                Level.DEBUG -> Timber.tag(KOIN_TAG).d(msg)
                Level.INFO -> Timber.tag(KOIN_TAG).i(msg)
                Level.ERROR -> Timber.tag(KOIN_TAG).e(msg)
                Level.NONE -> Timber.tag(KOIN_TAG).v(msg)
            }
        }
    }
}