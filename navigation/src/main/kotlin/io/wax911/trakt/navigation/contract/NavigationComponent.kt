/*
 * Copyright (C) 2020  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.wax911.trakt.navigation.contract

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import timber.log.Timber

/**
 * Intermediate construct for navigation components
 */
@OptIn(KoinApiExtension::class)
abstract class NavigationComponent : INavigationRouter, KoinComponent {

    private val moduleTag = javaClass.simpleName

    val bundleKey = "$moduleTag:PARAM"

    /**
     * Starts a new activity using the given [context] and [params]
     */
    override fun invoke(context: Context, params: Parcelable?) {
        runCatching {
            val intent = feature.screen(context)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(bundleKey, params)
            context.startActivity(intent)
        }.onFailure {
            Timber.tag(moduleTag).w(it)
        }
    }
}