package io.wax911.trakt.data.arch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.wax911.trakt.data.arch.database.converter.TraktTrendTypeConverters
import io.wax911.trakt.data.arch.database.migration.migrations
import io.wax911.trakt.data.show.entity.ShowEntity
import io.wax911.trakt.data.tmdb.entity.TmdbImageEntity

@Database(
    entities = [
        ShowEntity::class, TmdbImageEntity::class
    ],
    version = 1
)
@TypeConverters(TraktTrendTypeConverters::class)
internal abstract class TraktTrendRoomSource : RoomDatabase(), ITraktTrendDatabase {

    companion object {

        fun newInstance(context: Context): ITraktTrendDatabase {
            return Room.databaseBuilder(
                context,
                TraktTrendRoomSource::class.java,
                "trakt-trend"
            ).fallbackToDestructiveMigration()
                .addMigrations(*migrations)
                .build()
        }
    }
}
