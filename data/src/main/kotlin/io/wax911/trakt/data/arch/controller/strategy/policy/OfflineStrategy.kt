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

package io.wax911.trakt.data.arch.controller.strategy.policy

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.error.RequestError
import io.wax911.trakt.data.arch.controller.strategy.contract.ControllerStrategy
import timber.log.Timber

/**
 * Does not run any connectivity check before prior to execution, this is useful for sources
 * that may have caching on the network level through interception or cache-control from
 * the origin server.
 */
internal class OfflineStrategy<D> private constructor() : ControllerStrategy<D>() {

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
            block()
        }.onSuccess {result ->
            callback.recordSuccess()
            return result
        }.onFailure { exception ->
            Timber.tag(moduleTag).e(exception)
            when (exception) {
                is RequestError -> callback.recordFailure(exception)
                else -> callback.recordFailure(exception.generateForError())
            }
        }

        return null
    }

    companion object {
        internal fun <T> create() = OfflineStrategy<T>()
    }
}