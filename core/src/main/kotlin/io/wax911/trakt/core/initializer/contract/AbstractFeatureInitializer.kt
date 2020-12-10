package io.wax911.trakt.core.initializer.contract

import androidx.startup.Initializer
import co.anitrend.arch.extension.coroutine.ISupportCoroutine
import co.anitrend.arch.extension.coroutine.extension.Main
import io.wax911.trakt.core.initializer.KoinInitializer
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Contract for feature initializer with coroutine support for deferred initialization
 */
abstract class AbstractFeatureInitializer<T> : Initializer<T>, ISupportCoroutine by Main() {

    /**
     * @return A list of dependencies that this [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     *
     * By default a feature initializer should only start after [KoinInitializer] has been initialized
     */
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(KoinInitializer::class.java)
    }
}