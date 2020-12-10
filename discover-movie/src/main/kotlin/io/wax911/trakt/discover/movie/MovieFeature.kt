package io.wax911.trakt.discover.movie

import android.content.Context
import android.content.Intent
import io.wax911.trakt.discover.movie.view.content.MovieListContent
import io.wax911.trakt.discover.movie.view.screen.MovieScreen
import io.wax911.trakt.navigation.NavMovie

internal class MovieFeature : NavMovie.Provider {
    override fun screen(context: Context): Intent {
        val intent = Intent(context, MovieScreen::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return intent
    }

    override fun content() = MovieListContent::class.java
}