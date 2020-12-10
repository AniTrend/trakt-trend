package io.wax911.trakt.data.show.mapper

import com.uwetrottmann.trakt5.entities.Show
import io.wax911.trakt.data.arch.mapper.DefaultMapper
import io.wax911.trakt.data.arch.railway.OutCome
import io.wax911.trakt.data.arch.railway.extension.evaluate
import io.wax911.trakt.data.arch.railway.extension.otherwise
import io.wax911.trakt.data.arch.railway.extension.then
import io.wax911.trakt.data.show.converter.ShowModelConverter
import io.wax911.trakt.data.show.datasource.local.ShowLocalSource
import io.wax911.trakt.data.show.entity.ShowEntity

internal class ShowMapper(
    private val localSource: ShowLocalSource,
    private val converter: ShowModelConverter
) : DefaultMapper<List<Show>, List<ShowEntity>>() {

    /**
     * Handles the persistence of [data] into a local source
     *
     * @return [OutCome.Pass] or [OutCome.Fail] of the operation
     */
    override suspend fun persistChanges(data: List<ShowEntity>): OutCome<Nothing?> {
        return runCatching {
            localSource.upsert(data)
            OutCome.Pass(null)
        }.getOrElse { OutCome.Fail(listOf(it)) }
    }

    /**
     * Inserts the given object into the implemented room database,
     *
     * @param mappedData mapped object from [onResponseMapFrom] to insert into the database
     */
    override suspend fun onResponseDatabaseInsert(mappedData: List<ShowEntity>) {
        mappedData evaluate
                ::checkValidity then
                ::persistChanges otherwise
                ::handleException
    }

    /**
     * Creates mapped objects and handles the database operations which may be required to map various objects,
     *
     * @param source the incoming data source type
     * @return mapped object that will be consumed by [onResponseDatabaseInsert]
     */
    override suspend fun onResponseMapFrom(
        source: List<Show>
    ) = converter.convertFrom(source)

}