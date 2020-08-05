/*
 *    Copyright 2020 AniTrend
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.wax911.trakt.data.arch.controller.policy

import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.error.RequestError
import co.anitrend.arch.domain.entities.NetworkState
import io.wax911.trakt.data.arch.controller.strategy.ControllerStrategy
import timber.log.Timber

/**
 * Does not run any connectivity check before prior to execution, this is useful for sources
 * that may have caching on the network level through interception or cache-control from
 * the origin server.
 */
internal class OfflineControllerPolicy<D> private constructor() : ControllerStrategy<D>() {

    /**
     * Execute a paging task under an implementation strategy
     *
     * @param block What will be executed
     * @param requestCallback Event emitter
     */
    override suspend fun invoke(
        requestCallback: RequestCallback,
        block: suspend () -> D?
    ): D? = runCatching {
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
        return runCatching{
            networkState.postValue(NetworkState.Loading)
            val result = block()
            networkState.postValue(NetworkState.Success)
            result
        }.onFailure {
            networkState.postValue(
                NetworkState.Error(
                    heading = it.cause?.message ?: "Unexpected error encountered \uD83E\uDD2D",
                    message = it.message
                )
            )
        }.getOrNull()
    }

    companion object {
        fun <T> create() =
            OfflineControllerPolicy<T>()
    }
}