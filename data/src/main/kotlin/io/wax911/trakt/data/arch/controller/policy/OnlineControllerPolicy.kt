package io.wax911.trakt.data.arch.controller.policy

import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.error.RequestError
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.network.SupportConnectivity
import io.wax911.trakt.data.arch.controller.strategy.ControllerStrategy
import timber.log.Timber

/**
 * Runs connectivity check before prior to execution
 */
internal class OnlineControllerPolicy<D> private constructor(
    private val connectivity: SupportConnectivity
) : ControllerStrategy<D>() {

    /**
     * Execute a paging task under an implementation strategy
     *
     * @param block what will be executed
     * @param requestCallback paging event emitter
     */
    override suspend fun invoke(
        requestCallback: RequestCallback,
        block: suspend () -> D?
    ): D? {
        if (!connectivity.isConnected) {
            requestCallback.recordFailure(
                RequestError(
                    "No internet connection",
                    "Please check your internet connection",
                    null
                )
            )
            return null
        }

        return runCatching {
            val result = block()
            requestCallback.recordSuccess()
            result
        }.onFailure { e ->
            Timber.tag(moduleTag).e(e)
            requestCallback.recordFailure(
                RequestError(
                    "Unexpected error encountered \uD83E\uDD2D",
                    e.message,
                    e.cause
                )
            )
        }.getOrNull()
    }

    /**
     * Execute a task under an implementation strategy
     *
     * @param block what will be executed
     * @param networkState network state event emitter
     */
    override suspend fun invoke(
        networkState: MutableLiveData<NetworkState>,
        block: suspend () -> D?
    ): D? {
        if (connectivity.isConnected) {
            return runCatching{
                networkState.postValue(NetworkState.Loading)
                val result = block()
                networkState.postValue(NetworkState.Success)
                result
            }.getOrElse {
                Timber.tag(moduleTag).e(it)
                networkState.postValue(
                    NetworkState.Error(
                        heading = it.cause?.message ?: "Unexpected error encountered \uD83E\uDD2D",
                        message = it.message
                    )
                )
                null
            }
        } else {
            networkState.postValue(
                NetworkState.Error(
                    heading = "No internet connection detected \uD83E\uDD2D",
                    message = "Please check your internet connection"
                )
            )
        }
        return null
    }

    companion object {
        fun <T> create(connectivity: SupportConnectivity) =
            OnlineControllerPolicy<T>(
                connectivity
            )
    }
}