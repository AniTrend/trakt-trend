package io.wax911.trakt.navigation.feature

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

/**
 * Contract for services that a feature can provide
 */
interface INavigationFeature {
    fun screen(context: Context): Intent
    fun content(): Class<out Fragment>
}