package io.wax911.trakt.data.tmdb.mapper

import com.uwetrottmann.tmdb2.entities.TvShow
import io.wax911.trakt.data.arch.mapper.DefaultMapper
import io.wax911.trakt.data.arch.railway.OutCome
import io.wax911.trakt.data.arch.railway.extension.evaluate
import io.wax911.trakt.data.arch.railway.extension.otherwise
import io.wax911.trakt.data.arch.railway.extension.then
import io.wax911.trakt.data.tmdb.converter.TmdbModelConverter
import io.wax911.trakt.data.tmdb.datasource.local.TmdbLocalSource
import io.wax911.trakt.data.tmdb.entity.TmdbImageEntity
import io.wax911.trakt.data.tmdb.model.TmdbImageModel

internal class TmdbShowMapper(
    private val localSource: TmdbLocalSource,
    private val converter: TmdbModelConverter
): DefaultMapper<TvShow, List<TmdbImageEntity>>() {

    /**
     * Handles the persistence of [data] into a local source
     *
     * @return [OutCome.Pass] or [OutCome.Fail] of the operation
     */
    override suspend fun persistChanges(data: List<TmdbImageEntity>): OutCome<Nothing?> {
        return runCatching {
            localSource.upsert(data)
            OutCome.Pass(null)
        }.getOrElse { OutCome.Fail(listOf(it)) }
    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     * called in [retrofit2.Callback.onResponse] after assuring that the response was a success
     *
     * @param source the incoming data source type
     * @return Mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(source: TvShow): List<TmdbImageEntity> {
        val result = mutableListOf<TmdbImageEntity>()
        source.images?.posters?.mapTo(result) { image ->
            converter.convertFrom(
                TmdbImageModel(
                    parentId = source.id.toLong(),
                    type = TmdbImageModel.ImageType.POSTER,
                    image = image,
                    isPrimary = image.file_path == source.poster_path
                )
            )
        }

        source.images?.backdrops?.mapTo(result) { image ->
            converter.convertFrom(
                TmdbImageModel(
                    parentId = source.id.toLong(),
                    type = TmdbImageModel.ImageType.BACKDROP,
                    image = image,
                    isPrimary = image.file_path == source.backdrop_path
                )
            )
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
        mappedData evaluate
                ::checkValidity then
                ::persistChanges otherwise
                ::handleException
    }
}