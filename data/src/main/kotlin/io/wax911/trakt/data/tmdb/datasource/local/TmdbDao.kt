package io.wax911.trakt.data.tmdb.datasource.local

import androidx.room.Dao
import androidx.room.Query
import co.anitrend.arch.data.dao.ISupportQuery
import io.wax911.trakt.data.tmdb.entity.TmdbImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal abstract class TmdbDao: ISupportQuery<TmdbImageEntity> {

    @Query("""
        select count(id) from TmdbImageEntity where id = :tmdb
        """)
    abstract suspend fun count(tmdb: Int): Int

    @Query("""
        select * from TmdbImageEntity  where id = :tmdb
    """)
    abstract fun getByIdFlow(tmdb: Int): Flow<List<TmdbImageEntity>>

    @Query("""
        select * from TmdbImageEntity  where id = :tmdb
    """)
    abstract suspend fun getById(tmdb: Int): List<TmdbImageEntity>

    @Query("""
        delete from TmdbImageEntity where id = :tmdb
    """)
    abstract suspend fun deleteAll(tmdb: Int)

    @Query("""
        delete from TmdbImageEntity
    """)
    abstract suspend fun deleteAll()
}