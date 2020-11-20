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
import io.wax911.trakt.navigation.feature.INavigationFeature

internal interface INavigationRouter {

    /**
     * Feature provider contract
     */
    val feature: INavigationFeature

    /**
     * Starts a new activity using the given [context] and [params]
     */
    operator fun invoke(context: Context, params: Parcelable?)
}