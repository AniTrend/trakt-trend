package io.wax911.trakt.domain.repositories.film

import co.anitrend.arch.domain.state.UiState

interface FilmRepository<R: UiState<*>> {

    ///**
    // * Returns the most anticipated movies based on the number of lists a show appears on.
    // */
    //fun getAnticipatedMovies(): R
//
    ///**
    // * Returns the most collected (unique users) movies in the specified [period],
    // * defaulting to [TimePeriod.WEEKLY]. All stats are relative to the specific [TimePeriod].
    // *
    // * @param period Time period
    // */
    //fun getCollectedMovies(period: TimePeriod = TimePeriod.WEEKLY): R
//
    ///**
    // * Returns the most played (a single user can watch multiple episodes multiple times)
    // * movies in the specified [period], defaulting to [TimePeriod.WEEKLY].
    // * All stats are relative to the specific [TimePeriod].
    // *
    // * @param period Time period
    // */
    //fun getPlayedMovies(period: TimePeriod= TimePeriod.WEEKLY): R

    /**
     * Returns the most popular movies. Popularity is calculated using the rating percentage
     * and the number of ratings.
     */
    fun getPopularMovies(): R

    ///**
    // * Returns all movies being watched right now. Movies with the most users are returned first.
    // */
    //fun getTrendingMovies(): R
//
    ///**
    // * Returns all movies updated since the specified UTC date.
    // * We recommended storing the date you can be efficient using this method moving forward.
    // * By default, 10 results are returned. You can send a limit to get up to 100 results per page.
    // *
    // * @param startingFrom Updated since this date in the [UTC date format](https://www.w3.org/TR/NOTE-datetime)
    // */
    //fun getUpdatedMovies(startingFrom: String): R
//
    ///**
    // * Returns the most watched (unique users) movies in the specified [period],
    // * defaulting to [TimePeriod.WEEKLY]. All stats are relative to the specific [TimePeriod].
    // *
    // * @param period Time period
    // */
    //fun getWatchedMovies(period: TimePeriod = TimePeriod.WEEKLY): R
}