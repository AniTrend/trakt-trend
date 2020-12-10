package io.wax911.trakt.data.arch.database.converter

import androidx.room.TypeConverter
import io.wax911.trakt.data.arch.database.extensions.fromCommaSeparatedValues
import io.wax911.trakt.data.arch.database.extensions.fromEnum
import io.wax911.trakt.data.arch.database.extensions.toCommaSeparatedValues
import io.wax911.trakt.data.arch.database.extensions.toEnum
import io.wax911.trakt.data.tmdb.model.TmdbImageModel
import io.wax911.trakt.domain.models.MediaType

internal object TraktTrendTypeConverters {

    @TypeConverter
    @JvmStatic
    fun fromListOfString(source: List<String>) = source.toCommaSeparatedValues()

    @TypeConverter
    @JvmStatic
    fun toListOfString(source: String) = source.fromCommaSeparatedValues()

    @TypeConverter
    @JvmStatic
    fun fromShowType(source: MediaType) = source.fromEnum()

    @TypeConverter
    @JvmStatic
    fun toShowType(source: String)= source.toEnum<MediaType>()

    @TypeConverter
    @JvmStatic
    fun fromShowImageType(source: TmdbImageModel.ImageType) = source.fromEnum()

    @TypeConverter
    @JvmStatic
    fun toShowImageType(source: String)= source.toEnum<TmdbImageModel.ImageType>()

}