package io.wax911.trakt.data.tmdb.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import co.anitrend.arch.data.source.contract.ISourceObservable
import co.anitrend.arch.extension.SupportDispatchers
import co.anitrend.arch.extension.network.SupportConnectivity
import com.uwetrottmann.tmdb2.services.TvService
import io.wax911.trakt.data.arch.controller.policy.OnlineControllerPolicy
import io.wax911.trakt.data.arch.controller.strategy.ControllerStrategy
import io.wax911.trakt.data.arch.extensions.controller
import io.wax911.trakt.data.tmdb.datasource.local.TmdbDao
import io.wax911.trakt.data.tmdb.entity.TmdbImageEntity
import io.wax911.trakt.data.tmdb.mapper.TmbdShowMapper
import io.wax911.trakt.data.tmdb.source.contract.TmdbSource
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class TmdbSourceImpl(
    private val localSource: TmdbDao,
    private val remoteSource: TvService,
    private val connectivity: SupportConnectivity,
    dispatchers: SupportDispatchers
) : TmdbSource(dispatchers) {

    override val flow: Flow<List<IShowImage>>
        get() {
            return localSource.getByIdFlow(tmdbId)
                .map {
                    it.mapTo(mutableListOf()) { entity ->
                        TmdbImageEntity.transform(entity)
                    }
                }
        }

    override val observable =
        object : ISourceObservable<Nothing?, List<IShowImage>> {
            /**
             * Returns the appropriate observable which we will monitor for updates,
             * common implementation may include but not limited to returning
             * data source live data for a database
             *
             * @param parameter to use when executing
             */
            override fun invoke(parameter: Nothing?): LiveData<List<IShowImage>> {
                return flow.asLiveData(coroutineContext)
            }
        }

    override suspend fun findShowMatching(tmdb: Int): List<IShowImage> {
        val call = remoteSource.tv(tmdbId, "")

        val mapper = TmbdShowMapper(localSource)

        val controller =
            mapper.controller(
                OnlineControllerPolicy.create(
                    connectivity
                ),
                dispatchers
            )

        val imageEntities = controller(call, networkState).orEmpty()

        return imageEntities.mapTo(mutableListOf()) { entity ->
            TmdbImageEntity.transform(entity)
        }
    }

    /**
     * Clears data sources (databases, preferences, e.t.c)
     */
    override suspend fun clearDataSource() {
        localSource.deleteAll(tmdbId)
    }
}