package io.wax911.trakt.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.wax911.trakt.data.BuildConfig
import io.wax911.trakt.data.datasource.local.converter.*
import io.wax911.trakt.data.datasource.local.migration.MIGRATION_1_2
import io.wax911.trakt.data.datasource.local.migration.MIGRATION_2_3
import io.wax911.trakt.data.datasource.local.query.MovieDao
import io.wax911.trakt.data.datasource.local.query.ShowDao
import io.wax911.trakt.data.entitiy.movie.MovieEntity
import io.wax911.trakt.data.entitiy.show.ShowEntity

@Database(
    entities = [
        ShowEntity::class, MovieEntity::class
    ],
    version = 1
)
@TypeConverters(
    MovieIdsConverter::class, StatusConverter::class, ShowIdsConverter::class,
    AirsConverter::class, StringListConverter::class
)
abstract class TraktTrendDatabase : RoomDatabase() {

    abstract fun showDao(): ShowDao
    abstract fun movieDao(): MovieDao

    companion object {

        fun newInstance(context: Context): TraktTrendDatabase {
            return Room.databaseBuilder(
                context,
                TraktTrendDatabase::class.java,
                "trakt-trend"
            ).fallbackToDestructiveMigration()
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .build()
        }
    }
}
