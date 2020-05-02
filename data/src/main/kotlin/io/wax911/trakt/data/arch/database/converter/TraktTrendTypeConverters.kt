package io.wax911.trakt.data.arch.database.converter

import androidx.room.TypeConverter
import io.wax911.trakt.data.arch.database.extensions.fromCommaSeparatedValues
import io.wax911.trakt.data.arch.database.extensions.fromEnum
import io.wax911.trakt.data.arch.database.extensions.toCommaSeparatedValues
import io.wax911.trakt.data.arch.database.extensions.toEnum
import io.wax911.trakt.data.tmdb.enums.TmdbImageType
import io.wax911.trakt.data.show.enums.ShowType

internal object TraktTrendTypeConverters {

    @TypeConverter
    @JvmStatic
    fun fromListOfString(source: List<String>) = source.toCommaSeparatedValues()

    @TypeConverter
    @JvmStatic
    fun toListOfString(source: String) = source.fromCommaSeparatedValues()

    @TypeConverter
    @JvmStatic
    fun fromShowType(source: ShowType) = source.fromEnum()

    @TypeConverter
    @JvmStatic
    fun toShowType(source: String)= source.toEnum<ShowType>()

    @TypeConverter
    @JvmStatic
    fun fromShowImageType(source: TmdbImageType) = source.fromEnum()

    @TypeConverter
    @JvmStatic
    fun toShowImageType(source: String)= source.toEnum<TmdbImageType>()

}