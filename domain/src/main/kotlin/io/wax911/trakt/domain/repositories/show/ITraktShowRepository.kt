package io.wax911.trakt.domain.repositories.show

import co.anitrend.arch.domain.common.IUserInterfaceState

interface ITraktShowRepository<R: IUserInterfaceState<*>> {

    /**
     * @return popular shows
     */
    fun getPopularShows(): R

    /**
     * @return trending shows
     */
    fun getTrendingShows(): R
}