package io.wax911.trakt.core.settings

import android.content.Context
import co.anitrend.arch.extension.preference.*
import co.anitrend.arch.extension.preference.contract.ISupportPreference
import co.anitrend.arch.extension.settings.BooleanSetting
import co.anitrend.arch.extension.settings.IntSetting
import co.anitrend.arch.extension.settings.LongSetting
import io.wax911.trakt.core.R
import io.wax911.trakt.data.authentication.settings.IAuthenticationSettings

class Settings(context: Context) : SupportPreference(context), IAuthenticationSettings {

    override val authenticatedUserId = LongSetting(
        R.string.setting_authenticated_user_id,
        IAuthenticationSettings.INVALID_USER_ID,
        context.resources,
        this
    )

    override val isNewInstallation = BooleanSetting(
        R.string.setting_is_new_installation,
        true,
        context.resources,
        this
    )

    override val versionCode = IntSetting(
        R.string.setting_version_code,
        1,
        context.resources,
        this
    )



    companion object {
        val BINDINGS = arrayOf(
            Settings::class, ISupportPreference::class, IAuthenticationSettings::class
        )
    }
}