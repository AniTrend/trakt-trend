package io.wax911.trakt.data.arch.controller.strategy.policy

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.error.RequestError
import co.anitrend.arch.extension.network.SupportConnectivity
import io.wax911.trakt.data.arch.controller.strategy.contract.ControllerStrategy
import timber.log.Timber

/**
 * Runs connectivity check before prior to execution
 */
internal class OnlineStrategy<D> private constructor(
    private val connectivity: SupportConnectivity
) : ControllerStrategy<D>() {

    /**
     * Execute a task under an implementation strategy
     *
     * @param callback event emitter
     * @param block what will be executed
     */
    override suspend fun invoke(
        callback: RequestCallback,
        block: suspend () -> D?
    ): D? {
        runCatching {
            if (connectivity.isConnected)
                block()
            else
                throw RequestError(
                    "No internet connectivity detected",
                    "Please make sure that your device has an active internet connection"
                )
        }.onSuccess { result ->
            callback.recordSuccess()
            return result
        }.onFailure { exception ->
            Timber.tag(moduleTag).w(exception)
            when (exception) {
                is RequestError -> callback.recordFailure(exception)
                else -> callback.recordFailure(exception.generateForError())
            }
        }

        return null
    }

    companion object {
        internal fun <T> create(
            connectivity: SupportConnectivity,
        ) = OnlineStrategy<T>(connectivity)
    }
}