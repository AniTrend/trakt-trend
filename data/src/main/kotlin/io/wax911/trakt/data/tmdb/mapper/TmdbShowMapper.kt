package io.wax911.trakt.data.tmdb.mapper

import com.uwetrottmann.tmdb2.entities.Image
import com.uwetrottmann.tmdb2.entities.TvShow
import io.wax911.trakt.data.arch.mapper.TraktTrendMapper
import io.wax911.trakt.data.tmdb.datasource.local.TmdbDao
import io.wax911.trakt.data.tmdb.entity.TmdbImageEntity
import io.wax911.trakt.data.tmdb.enums.TmdbImageType
import io.wax911.trakt.data.tmdb.extensions.generateIdFromPath

internal class TmdbShowMapper(
    private val localDao: TmdbDao
): TraktTrendMapper<TvShow, List<TmdbImageEntity>>() {
    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: TvShow): List<TmdbImageEntity> {
        fun mapImage(image: Image, type: TmdbImageType): TmdbImageEntity {
            return TmdbImageEntity(
                id = image.file_path!!.generateIdFromPath(),
                showId = source.id!!.toLong(),
                path = image.file_path!!,
                type = type,
                language = image.iso_639_1,
                rating = image.vote_average?.toFloat() ?: 0f,
                isPrimary = when (type) {
                    TmdbImageType.BACKDROP -> image.file_path == source.backdrop_path
                    TmdbImageType.POSTER -> image.file_path == source.poster_path
                    else -> false
                }
            )
        }


        val result = mutableListOf<TmdbImageEntity>()
        source.images?.posters?.mapTo(result) {
            mapImage(it, TmdbImageType.POSTER)
        }
        source.images?.backdrops?.mapTo(result) {
            mapImage(it, TmdbImageType.BACKDROP)
        }
        return result
    }

    /**
     * Inserts the given object into the implemented room database,
     * called in [retrofit2.Callback.onResponse]
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<TmdbImageEntity>) {
        if (mappedData.isNotEmpty())
            localDao.upsert(mappedData)
    }
}