package io.wax911.trakt.data.authentication.settings

import co.anitrend.arch.extension.settings.contract.AbstractSetting

interface IAuthenticationSettings {

    val authenticatedUserId: AbstractSetting<Long>

    companion object  {
        const val INVALID_USER_ID: Long = -1
    }
}