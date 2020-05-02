package io.wax911.trakt.data.tmdb.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import co.anitrend.arch.data.mapper.contract.ISupportMapperHelper
import io.wax911.trakt.data.show.entity.ShowEntity
import io.wax911.trakt.data.tmdb.enums.TmdbImageType
import io.wax911.trakt.domain.entities.contract.IEntity
import io.wax911.trakt.domain.entities.image.ShowImage
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.image.enums.ShowImageType

@Entity(
    indices = [
        Index(value = ["showId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = ShowEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("showId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class TmdbImageEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    val showId: Long,
    val path: String,
    val type: TmdbImageType,
    val language: String? = null,
    val rating: Float,
    val isPrimary: Boolean
) : IEntity {

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is TmdbImageEntity -> { other.id == id }
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + showId.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (language?.hashCode() ?: 0)
        result = 31 * result + rating.hashCode()
        result = 31 * result + isPrimary.hashCode()
        return result
    }

    companion object : ISupportMapperHelper<TmdbImageEntity, IShowImage> {
        /**
         * Transforms the the [source] to the target type
         */
        override fun transform(source: TmdbImageEntity): IShowImage {
            return ShowImage(
                path = source.path,
                type = ShowImageType.valueOf(source.type.name),
                language = source.language,
                rating = source.rating,
                isPrimary = source.isPrimary,
                id = source.id
            )
        }
    }
}