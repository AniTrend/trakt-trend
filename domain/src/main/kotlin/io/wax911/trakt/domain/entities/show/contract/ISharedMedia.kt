package io.wax911.trakt.domain.entities.show.contract

import io.wax911.trakt.domain.entities.contract.IEntity

interface ISharedMedia : IEntity {
    val title: String
    val rating: Double
    val votes: Int
    val updated: Long
    val year: Int
}