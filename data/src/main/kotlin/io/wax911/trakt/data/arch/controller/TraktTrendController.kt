package io.wax911.trakt.data.arch.controller

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingRequestHelper
import co.anitrend.arch.data.common.ISupportPagingResponse
import co.anitrend.arch.data.common.ISupportResponse
import co.anitrend.arch.domain.entities.NetworkState
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
) : ISupportResponse<Call<S>, D>, ISupportPagingResponse<Call<S>> {

    /**
     * Response handler for coroutine contexts which need to observe [NetworkState]
     *
     * @param resource awaiting execution
     * @param networkState for the deferred result
     *
     * @return resource fetched if present
     */
    override suspend fun invoke(
        resource: Call<S>,
        networkState: MutableLiveData<NetworkState>
    ): D? {
        return strategy({
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
        }, networkState)
    }

    /**
     * Response handler for coroutine contexts, mainly for paging
     *
     * @param resource awaiting execution
     * @param pagingRequestHelper optional paging request callback
     */
    override suspend fun invoke(
        resource: Call<S>,
        pagingRequestHelper: PagingRequestHelper.Request.Callback
    ) {
        strategy({
            val responseBody = withContext(dispatchers.io) {
                resource.fetchBodyWithRetry()
            }
            val mapped = withContext(dispatchers.computation) {
                responseMapper.onResponseMapFrom(responseBody)
            }
            withContext(dispatchers.io) {
                responseMapper.onResponseDatabaseInsert(mapped)
            }
        }, pagingRequestHelper)
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