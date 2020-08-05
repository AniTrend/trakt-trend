package io.wax911.trakt.data.arch.controller

import co.anitrend.arch.data.common.ISupportResponse
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import io.wax911.trakt.data.arch.controller.strategy.ControllerStrategy
import io.wax911.trakt.data.arch.extensions.fetchBodyWithRetry
import io.wax911.trakt.data.arch.mapper.TraktTrendMapper
import kotlinx.coroutines.withContext
import retrofit2.Call

internal class TraktTrendController<S, D> private constructor(
    private val responseMapper: TraktTrendMapper<S, D>,
    private val strategy: ControllerStrategy<D>,
    private val dispatchers: SupportDispatchers
) : ISupportResponse<Call<S>, D> {

    /**
     * Response handler for coroutine contexts, mainly for paging
     *
     * @param resource awaiting execution
     * @param requestCallback optional paging request callback
     */
    override suspend fun invoke(resource: Call<S>, requestCallback: RequestCallback): D? {
        return strategy(requestCallback) {
            val responseBody = withContext(dispatchers.io) {
                resource.fetchBodyWithRetry()
            }
            val mapped = withContext(dispatchers.computation) {
                responseMapper.onResponseMapFrom(responseBody)
            }
            withContext(dispatchers.io) {
                responseMapper.onResponseDatabaseInsert(mapped)
            }
            mapped
        }
    }

    /**
     * Response handler for coroutine contexts
     *
     * @param resource awaiting execution
     */
    suspend operator fun invoke(resource: Call<S>): D? {
        val responseBody = withContext(dispatchers.io) {
            resource.fetchBodyWithRetry()
        }
        val mapped = withContext(dispatchers.computation) {
            responseMapper.onResponseMapFrom(responseBody)
        }
        withContext(dispatchers.io) {
            responseMapper.onResponseDatabaseInsert(mapped)
        }
        return mapped
    }


    companion object {
        fun <S, D> newInstance(
            responseMapper: TraktTrendMapper<S, D>,
            strategy: ControllerStrategy<D>,
            supportDispatchers: SupportDispatchers
        ) = TraktTrendController(
            responseMapper,
            strategy,
            supportDispatchers
        )
    }
}