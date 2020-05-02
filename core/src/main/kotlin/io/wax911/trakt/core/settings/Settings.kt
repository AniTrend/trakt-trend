package io.wax911.trakt.core.settings

import android.content.Context
import co.anitrend.arch.extension.preference.*
import io.wax911.trakt.core.R
import io.wax911.trakt.data.authentication.settings.IAuthenticationSettings

class Settings(context: Context) : SupportSettings(context),
    IAuthenticationSettings {

    override var authenticatedUserId by LongPreference(
        R.string.setting_authenticated_user_id,
        IAuthenticationSettings.INVALID_USER_ID,
        context.resources
    )

    override var isNewInstallation by BooleanPreference(
        R.string.setting_is_new_installation,
        true,
        context.resources
    )

    override var versionCode by IntPreference(
        R.string.setting_version_code,
        1,
        context.resources
    )

    companion object {
        val BINDINGS = arrayOf(
            Settings::class, SupportSettings::class, IAuthenticationSettings::class
        )
    }
}