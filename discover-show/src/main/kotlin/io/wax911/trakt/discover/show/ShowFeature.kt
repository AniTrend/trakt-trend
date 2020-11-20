package io.wax911.trakt.discover.show

import android.content.Context
import android.content.Intent
import io.wax911.trakt.discover.show.view.content.ShowListContent
import io.wax911.trakt.discover.show.view.screen.ShowScreen
import io.wax911.trakt.navigation.NavShow

internal class ShowFeature : NavShow.Provider {
    override fun screen(context: Context): Intent {
        val intent = Intent(context, ShowScreen::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return intent
    }

    override fun content() = ShowListContent::class.java
}