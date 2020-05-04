package io.wax911.trakt.core.helpers.coil.fetch

import coil.bitmappool.BitmapPool
import coil.decode.DataSource
import coil.decode.Options
import coil.fetch.FetchResult
import coil.fetch.Fetcher
import coil.fetch.SourceResult
import coil.map.MeasuredMapper
import coil.size.PixelSize
import coil.size.Size
import io.wax911.trakt.core.helpers.coil.model.ImageConfig
import io.wax911.trakt.data.tmdb.extensions.findHighestRatedPoster
import io.wax911.trakt.data.tmdb.helper.TmdbImageUrlProvider
import io.wax911.trakt.data.tmdb.source.contract.TmdbSource
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.image.contract.ITmdbImage
import io.wax911.trakt.domain.entities.image.enums.ShowImageType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.BufferedSource

class ShowImageFetcher(
    private val client: OkHttpClient,
    private val tmdbSource: TmdbSource,
    private val mapper: MeasuredMapper<IShowImage, HttpUrl>
) : Fetcher<ITmdbImage> {

    private suspend fun fetchImagesFromSource(data: ITmdbImage, size: Size): HttpUrl {
        val images= tmdbSource.get(data)
        val poster = ImageConfig(images).bestPostImage()
        return poster?.let { mapper.map(poster, size) } ?: FALLBACK_URL
    }

    private suspend fun fetchDataSourceUsing(httpUrl: HttpUrl): BufferedSource {
        val request = Request.Builder()
            .get().url(httpUrl).build()
        val call = client.newCall(request)
        val source = GlobalScope.async {
            @Suppress("BlockingMethodInNonBlockingContext")
            val response = call.execute()
            response.body?.source()
        }
        // this is dangerous :smug:
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
        val source = fetchDataSourceUsing(url)
        return SourceResult(
            source = source,
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
        val hash = data.type.name.hashCode() + data.id
        return hash.toString()
    }

    companion object {
        private val FALLBACK_URL = "https://trakt.tv/assets/logos/header@2x-09f929ba67b0964596b359f497884cd9.png".toHttpUrl()
    }
}