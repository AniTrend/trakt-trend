package io.wax911.trakt.domain.entities.image.contract

import io.wax911.trakt.domain.entities.contract.IEntity
import io.wax911.trakt.domain.entities.image.enums.ShowImageType

interface IShowImage : IEntity {
    val path: String
    val type: ShowImageType
    val language: String?
    val rating: Float
    val isPrimary: Boolean
}