package io.wax911.trakt.data.tmdb.usecase

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import com.uwetrottmann.tmdb2.services.ConfigurationService
import io.wax911.trakt.data.arch.TraktTrendExperimentalFeature
import io.wax911.trakt.data.arch.extensions.fetchBodyWithRetry
import io.wax911.trakt.data.tmdb.helper.TmdbImageUrlProvider
import kotlinx.coroutines.flow.Flow

class TmdbUseCaseManager(
    private val source: ConfigurationService
) {

    private val imageProviderSubject = MutableLiveData(TmdbImageUrlProvider())

    @TraktTrendExperimentalFeature
    val imageProvider: Flow<TmdbImageUrlProvider> = imageProviderSubject.asFlow()

    @TraktTrendExperimentalFeature
    suspend fun refreshConfiguration() {
        val configuration = source.configuration().fetchBodyWithRetry()
        configuration.images?.let { images ->
            val newProvider =
                TmdbImageUrlProvider(
                    images.secure_base_url!!,
                    images.poster_sizes ?: emptyList(),
                    images.backdrop_sizes ?: emptyList(),
                    images.logo_sizes ?: emptyList()
                )
            imageProviderSubject.postValue(newProvider)
        }
    }
}