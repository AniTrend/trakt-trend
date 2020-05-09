package io.wax911.trakt.core.presenter

import android.content.Context
import co.anitrend.arch.core.presenter.SupportPresenter
import io.wax911.trakt.core.settings.Settings

open class CorePresenter(
    context: Context,
    settings: Settings
): SupportPresenter<Settings>(context, settings)
