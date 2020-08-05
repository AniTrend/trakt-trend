package io.wax911.trakt.domain.repositories.series

import co.anitrend.arch.domain.state.UiState

interface SeriesRepository<R: UiState<*>> {

    ///**
    // * Returns the most anticipated shows based on the number of lists a show appears on.
    // */
    //fun getAnticipatedShows(): R
//
    ///**
    // * Returns the most collected (unique users) shows in the specified [period],
    // * defaulting to [TimePeriod.WEEKLY]. All stats are relative to the specific [TimePeriod].
    // *
    // * @param period Time period
    // */
    //fun getCollectedShows(period: TimePeriod = TimePeriod.WEEKLY): R
//
    ///**
    // * Returns the most played (a single user can watch multiple episodes multiple times)
    // * shows in the specified [period], defaulting to [TimePeriod.WEEKLY].
    // * All stats are relative to the specific [TimePeriod].
    // *
    // * @param period Time period
    // */
    //fun getPlayedShows(period: TimePeriod = TimePeriod.WEEKLY): R

    /**
     * Returns the most popular shows. Popularity is calculated using the rating percentage
     * and the number of ratings.
     */
    fun getPopularShows(): R

    ///**
    // * Returns all shows being watched right now. Shows with the most users are returned first.
    // */
    //fun getTrendingShows(): R
//
    ///**
    // * Returns all shows updated since the specified UTC date.
    // * We recommended storing the date you can be efficient using this method moving forward.
    // * By default, 10 results are returned. You can send a limit to get up to 100 results per page.
    // *
    // * @param startingFrom Updated since this date in the [UTC date format](https://www.w3.org/TR/NOTE-datetime)
    // */
    //fun getUpdatedShows(startingFrom: String): R
//
    ///**
    // * Returns the most watched (unique users) shows in the specified [period],
    // * defaulting to [TimePeriod.WEEKLY]. All stats are relative to the specific [TimePeriod].
    // *
    // * @param period Time period
    // */
    //fun getWatchedShows(period: TimePeriod = TimePeriod.WEEKLY): R
}