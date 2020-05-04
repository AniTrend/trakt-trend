package io.wax911.trakt.data.show.datasource.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import co.anitrend.arch.data.dao.ISupportQuery
import io.wax911.trakt.data.show.entity.ShowEntity
import io.wax911.trakt.domain.models.MediaType
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class ShowDao: ISupportQuery<ShowEntity> {

    @Query("""
        select count(id) from ShowEntity where showType = :showType
        """)
    abstract suspend fun count(showType: MediaType): Int

    @Query("""
        select * from ShowEntity where showType = :showType
    """)
    abstract fun getItems(showType: MediaType): DataSource.Factory<Int, ShowEntity>

    @Query("""
        select * from ShowEntity where showType = :showType order by votes desc
    """)
    abstract fun getPopular(showType: MediaType): DataSource.Factory<Int, ShowEntity>

    @Query("""
        select * from ShowEntity where showType = :showType order by watchers desc
    """)
    abstract fun getTrending(showType: MediaType): DataSource.Factory<Int, ShowEntity>

    @Query("""
        select * from ShowEntity  where showType = :showType and id = :id
    """)
    abstract fun getByIdFlow(id: Int, showType: MediaType): Flow<ShowEntity>

    @Query("""
        select * from ShowEntity  where showType = :showType and id = :id
    """)
    abstract fun getById(id: Int, showType: MediaType): ShowEntity

    @Query("""
        delete from ShowEntity where showType = :showType
    """)
    abstract suspend fun deleteAll(showType: MediaType)
}