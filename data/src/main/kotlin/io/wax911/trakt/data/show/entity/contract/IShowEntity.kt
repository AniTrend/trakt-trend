package io.wax911.trakt.data.show.entity.contract

internal interface IShowEntity : IShowCoreEntity {
    val airs: Long?
    val network: String?
    val country: String?
    val status: String?
}