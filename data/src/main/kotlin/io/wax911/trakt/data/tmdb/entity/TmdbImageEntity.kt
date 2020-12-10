package io.wax911.trakt.data.tmdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.wax911.trakt.data.tmdb.model.TmdbImageModel
import io.wax911.trakt.domain.entities.contract.IEntity

@Entity
internal data class TmdbImageEntity(
    @PrimaryKey
    override val id: Int,
    val showId: Long,
    val path: String,
    val type: TmdbImageModel.ImageType,
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
}