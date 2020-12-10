package io.wax911.trakt.core.initializer.contract

import androidx.startup.Initializer
import co.anitrend.arch.extension.coroutine.SupportCoroutine
import io.wax911.trakt.core.initializer.KoinInitializer
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Contract for feature initializer with coroutine support for deferred initialization
 */
abstract class AbstractFeatureInitializer<T> : Initializer<T>, SupportCoroutine {

    /**
     * Requires an instance of [kotlinx.coroutines.Job] or [kotlinx.coroutines.SupervisorJob]
     */
    final override val supervisorJob: Job = SupervisorJob()

    /**
     * @return A list of dependencies that this [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     *
     * By default a feature initializer should only start after [KoinInitializer] has been initialized
     */
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return listOf(KoinInitializer::class.java)
    }

    /**
     * Coroutine dispatcher specification
     *
     * @return one of the sub-types of [kotlinx.coroutines.Dispatchers]
     */
    final override val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default

    /**
     * Persistent context for the coroutine
     *
     * @return [kotlin.coroutines.CoroutineContext] preferably built from
     * [supervisorJob] + [coroutineDispatcher]
     */
    final override val coroutineContext: CoroutineContext = supervisorJob + coroutineDispatcher

    /**
     * A failure or cancellation of a child does not cause the supervisor job
     * to fail and does not affect its other children.
     *
     * @return [kotlinx.coroutines.CoroutineScope]
     */
    final override val scope: CoroutineScope = CoroutineScope(coroutineContext)
}