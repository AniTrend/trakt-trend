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

import io.wax911.trakt.navigation.contract.NavigationComponent

object NavigationTargets {

    object ShowScreen : NavigationComponent(
        packageName = "discover.show.view.screen",
        className = "ShowScreen"
    ) {
        const val PARAM = "ShowScreen:PARAM"
    }

    object ShowListContent : NavigationComponent(
        packageName = "discover.show.view.content",
        className = "ShowListContent"
    ) {
        const val PARAM = "ShowListContent:PARAM"
    }

    object ShowDetailContent : NavigationComponent(
        packageName = "discover.show.view.content",
        className = "ShowDetailContent"
    ) {
        const val PARAM = "ShowDetailContent:PARAM"
    }

    object MovieScreen : NavigationComponent(
        packageName = "discover.movie.view.screen",
        className = "MovieScreen"
    ) {
        const val PARAM = "MovieScreen:PARAM"
    }

    object MovieListContent : NavigationComponent(
        packageName = "discover.movie.view.content",
        className = "MovieListContent"
    ) {
        const val PARAM = "MovieListContent:PARAM"
    }

    object MovieDetailContent : NavigationComponent(
        packageName = "discover.movie.view.content",
        className = "MovieDetailContent"
    ) {
        const val PARAM = "MovieDetailContent:PARAM"
    }
}