package io.wax911.trakt.data.arch.controller.core

import co.anitrend.arch.data.common.ISupportResponse
import co.anitrend.arch.data.request.callback.RequestCallback
import io.wax911.trakt.data.arch.controller.strategy.contract.ControllerStrategy
import io.wax911.trakt.data.arch.mapper.DefaultMapper
import io.wax911.trakt.data.arch.network.contract.NetworkClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.Call


/**
 * General controller that handles complex logic of making requests, capturing errors,
 * notifying state observers and providing input to response mappers
 */
internal class DefaultController<S, out D>(
    private val mapper: DefaultMapper<S, D>,
    private val strategy: ControllerStrategy<D>,
    private val dispatcher: CoroutineDispatcher,
    private val client: NetworkClient<S>
) : ISupportResponse<Call<S>, D> {

    override suspend fun invoke(
        resource: Call<S>,
        requestCallback: RequestCallback
    ) = strategy(requestCallback) {
        val response = client.fetch(resource)

        val mapped = mapper.onResponseMapFrom(response)
        withContext(dispatcher) {
            mapper.onResponseDatabaseInsert(mapped)
        }
        mapped
    }
}