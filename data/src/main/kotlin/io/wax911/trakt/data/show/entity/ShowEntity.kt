package io.wax911.trakt.data.show.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.anitrend.arch.data.mapper.contract.ISupportMapperHelper
import io.wax911.trakt.data.show.entity.contract.IShowEntity
import io.wax911.trakt.data.show.entity.trending.ITrendingEntity
import io.wax911.trakt.data.show.enums.ShowType
import io.wax911.trakt.domain.entities.show.ShowItem
import io.wax911.trakt.domain.entities.contract.IEntity
import io.wax911.trakt.domain.entities.show.contract.ISharedMedia

@Entity
internal data class ShowEntity(
    override val airs: Long?,
    override val network: String?,
    override val country: String?,
    override val status: String?,
    override val title: String?,
    override var overview: String?,
    override var rating: Double?,
    override var votes: Int?,
    override var updated: Long?,
    override var translations: List<String>,
    override val year: Int?,
    override val certification: String?,
    override val tagLine: String? = null,
    override val released: Long? = null,
    override val runtime: Int?,
    override val trailer: String?,
    override val homepage: String?,
    override val language: String?,
    override val genres: List<String>,
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,
    override val traktId: Int?,
    override val tmdbId: Int?,
    override val showType: ShowType,
    override val watchers: Int? = null
): IShowEntity, ITrendingEntity {

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is IEntity -> other.id == id
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = airs?.hashCode() ?: 0
        result = 31 * result + (network?.hashCode() ?: 0)
        result = 31 * result + (country?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (overview?.hashCode() ?: 0)
        result = 31 * result + (rating?.hashCode() ?: 0)
        result = 31 * result + (votes ?: 0)
        result = 31 * result + (updated?.hashCode() ?: 0)
        result = 31 * result + translations.hashCode()
        result = 31 * result + (year ?: 0)
        result = 31 * result + (certification?.hashCode() ?: 0)
        result = 31 * result + (tagLine?.hashCode() ?: 0)
        result = 31 * result + (released?.hashCode() ?: 0)
        result = 31 * result + (runtime ?: 0)
        result = 31 * result + (trailer?.hashCode() ?: 0)
        result = 31 * result + (homepage?.hashCode() ?: 0)
        result = 31 * result + (language?.hashCode() ?: 0)
        result = 31 * result + genres.hashCode()
        result = 31 * result + id
        result = 31 * result + (traktId ?: 0)
        result = 31 * result + (tmdbId ?: 0)
        result = 31 * result + showType.hashCode()
        result = 31 * result + (watchers ?: 0)
        return result
    }

    companion object : ISupportMapperHelper<ShowEntity, ISharedMedia> {
        /**
         * Transforms the the [source] to the target type
         */
        override fun transform(source: ShowEntity): ISharedMedia {
            return ShowItem(
                title = source.title.orEmpty(),
                rating = source.rating ?: 0.0,
                votes = source.votes ?: 0,
                updated = source.updated ?: 0,
                year = source.year ?: 0,
                id = source.id
            )
        }
    }
}