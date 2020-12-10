/*
 * Copyright (C) 2019  AniTrend
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

package io.wax911.trakt.navigation

import android.os.Parcelable
import io.wax911.trakt.navigation.contract.NavigationComponent
import io.wax911.trakt.navigation.feature.INavigationFeature
import kotlinx.android.parcel.Parcelize
import org.koin.core.component.inject

object NavShow : NavigationComponent() {
    override val feature: INavigationFeature by inject<Provider>()

    interface Provider : INavigationFeature

    @Parcelize data class Params(
        val showId: Int
    ) : Parcelable
}

object NavMovie : NavigationComponent() {

    override val feature: INavigationFeature by inject<Provider>()

    interface Provider : INavigationFeature

    @Parcelize data class Params(
        val movieId: Int
    ) : Parcelable
}