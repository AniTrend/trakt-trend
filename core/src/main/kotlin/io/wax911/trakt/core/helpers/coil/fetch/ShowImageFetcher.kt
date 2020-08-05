package io.wax911.trakt.core.helpers.coil.fetch

import coil.bitmappool.BitmapPool
import coil.decode.DataSource
import coil.decode.Options
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.map.MeasuredMapper
import coil.size.Size
import io.wax911.trakt.core.helpers.coil.model.ImageConfig
import io.wax911.trakt.data.tmdb.source.contract.TmdbSource
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.image.contract.ITmdbImage
import io.wax911.trakt.domain.entities.image.enums.ShowImageType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.BufferedSource
import timber.log.Timber

class ShowImageFetcher(
    private val client: OkHttpClient,
    private val tmdbSource: TmdbSource,
    private val mapper: MeasuredMapper<IShowImage, HttpUrl>
) : Fetcher<ITmdbImage> {

    private suspend fun fetchImagesFromSource(data: ITmdbImage, size: Size): HttpUrl {
        return runCatching {
            val images = tmdbSource(data)
            Timber.tag(moduleTag).v(
                "Images retrieved from tmdb source for item: ${data.id} -> $size"
            )
            val imageConfig = ImageConfig(images)
            val poster = when (data.imageType) {
                ShowImageType.BACKDROP -> imageConfig.bestBackDropImage()
                ShowImageType.POSTER -> imageConfig.bestPosterImage()
                ShowImageType.LOGO -> imageConfig.bestLogoImage()
            }
            Timber.tag(moduleTag).v("Best image selected: ${poster?.path}")
            poster?.let { mapper.map(poster, size) }
        }.onFailure {
            Timber.tag(moduleTag).e(it)
        }.getOrNull() ?: FALLBACK_URL
    }

    private suspend fun fetchDataSourceUsing(httpUrl: HttpUrl): BufferedSource {
        val request = Request.Builder().get().url(httpUrl).build()
        val call = client.newCall(request)
        val source = GlobalScope.async {
            @Suppress("BlockingMethodInNonBlockingContext")
            val response = call.execute()
            response.body?.source()
        }
        // Coil should handle exceptions internally and
        // prevent them from leaking outside this scope (this is dangerous :smug:)
        return source.await()!!
    }

    /**
     * Load the [data] into memory. Perform any necessary fetching operations.
     *
     * @param pool A [BitmapPool] which can be used to request [Bitmap] instances.
     * @param data The data to load.
     * @param size The requested dimensions for the image.
     * @param options A set of configuration options for this request.
     */
    override suspend fun fetch(
        pool: BitmapPool,
        data: ITmdbImage,
        size: Size,
        options: Options
    ): FetchResult {
        val url = fetchImagesFromSource(data, size)
        return SourceResult(
            source = fetchDataSourceUsing(url),
            mimeType = "image/*",
            dataSource = DataSource.NETWORK
        )
    }

    /**
     * Compute the memory cache key for [data].
     *
     * Items with the same cache key will be treated as equivalent by the [MemoryCache].
     *
     * Returning null will prevent the result of [fetch] from being added to the memory cache.
     */
    override fun key(data: ITmdbImage): String? {
        val hash = data.id
        Timber.tag(moduleTag).v("Generated key for image: $hash")
        return hash.toString()
    }

    companion object {

        private val moduleTag = ShowImageFetcher::class.java.simpleName

        private val FALLBACK_URL =
            ("https://trakt.tv/assets/main/poster-bg@2x-c9b1fab0a183c593d63095bb454a73fe.jpg")
                .toHttpUrl()
    }
}