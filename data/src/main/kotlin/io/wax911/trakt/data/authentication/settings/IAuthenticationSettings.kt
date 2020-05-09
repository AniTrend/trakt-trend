package io.wax911.trakt.data.authentication.settings

interface IAuthenticationSettings {

    var authenticatedUserId: Long

    companion object  {
        const val INVALID_USER_ID: Long = -1
    }
}