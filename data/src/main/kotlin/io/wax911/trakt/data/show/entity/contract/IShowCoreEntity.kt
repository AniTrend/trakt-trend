package io.wax911.trakt.data.show.entity.contract

import io.wax911.trakt.data.arch.common.ITraktId
import io.wax911.trakt.domain.models.MediaType
import io.wax911.trakt.data.tmdb.entity.contract.ITmdbId
import io.wax911.trakt.domain.entities.contract.IEntity

internal interface IShowCoreEntity: IEntity, ITraktId, ITmdbId {
    val title: String?
    var overview: String?
    var rating: Double?
    var votes: Int?
    var updated: Long?
    var translations: List<String>

    val year: Int?
    val certification: String?
    val tagLine: String?
    val released: Long?
    val runtime: Int?
    val trailer: String?
    val homepage: String?
    val language: String?
    val genres: List<String>

    val showType: MediaType
}