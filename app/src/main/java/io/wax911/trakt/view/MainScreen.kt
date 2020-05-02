package io.wax911.trakt.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import co.anitrend.arch.ui.activity.SupportActivity
import co.anitrend.arch.ui.common.ISupportActionUp
import co.anitrend.arch.ui.fragment.SupportFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import io.wax911.trakt.R
import io.wax911.trakt.core.view.TraktTrendActivity
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.domain.usecases.MediaRequestType
import io.wax911.trakt.movie.ui.fragment.FragmentMovieList
import io.wax911.trakt.show.ui.fragment.FragmentShowList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import timber.log.Timber

class MainScreen : TraktTrendActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val bottomDrawerBehavior: BottomSheetBehavior<FrameLayout>?
        get() = BottomSheetBehavior.from(bottomNavigationDrawer)

    @IdRes
    private var selectedItem: Int = R.id.nav_popular_series

    @StringRes
    private var selectedTitle: Int = R.string.nav_popular_series

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottomAppBar)
        bottomDrawerBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    /**
     * Additional initialization to be done in this method, if the overriding class is type of [SupportFragment]
     * then this method will be called in [SupportFragment.onCreate]. Otherwise [SupportActivity.onPostCreate]
     * invokes this function
     *
     * @see [SupportActivity.onPostCreate] and [SupportFragment.onCreate]
     * @param
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        bottomAppBar.apply {
            setNavigationOnClickListener {
                bottomDrawerBehavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
        floatingShortcutButton.setOnClickListener {
            Toast.makeText(this, "Fab Clicked", Toast.LENGTH_SHORT).show()
        }
        bottomNavigationView.apply {
            launch {
                setNavigationItemSelectedListener(this@MainScreen)
            }
            setCheckedItem(selectedItem)
        }
        onUpdateUserInterface()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(key_navigation_selected, selectedItem)
        outState.putInt(key_navigation_title, selectedTitle)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        selectedItem = savedInstanceState.getInt(key_navigation_selected)
        selectedTitle = savedInstanceState.getInt(key_navigation_title)
    }

    override fun onBackPressed() {
        when (bottomDrawerBehavior?.state) {
            BottomSheetBehavior.STATE_EXPANDED,
            BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                bottomDrawerBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                return
            }
            else -> super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                Toast.makeText(this, "onMenuItemClick", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (selectedItem != item.itemId) {
            if (item.groupId != R.id.nav_group_customization) {
                selectedItem = item.itemId
                launch {
                    onNavigate(selectedItem)
                }
            } else launch{
                onNavigate(item.itemId)
            }
        }
        return true
    }

    private suspend fun onNavigate(@IdRes menu: Int) {
        var actionUpFragment: ISupportActionUp? = null
        when (menu) {
            R.id.nav_theme -> {
                when (AppCompatDelegate.getDefaultNightMode()) {
                    AppCompatDelegate.MODE_NIGHT_YES ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    else ->
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                recreate()
            }
            R.id.nav_about -> Toast.makeText(this@MainScreen, "About", Toast.LENGTH_SHORT).show()
            R.id.nav_contact -> Toast.makeText(this@MainScreen, "Contact", Toast.LENGTH_SHORT).show()
            R.id.nav_popular_series -> {
                selectedTitle = R.string.nav_popular_series
                actionUpFragment = FragmentShowList.newInstance(
                    Bundle().also {
                        it.putParcelable(
                            FragmentShowList.PARAM_SHOW_TYPE,
                            MediaPayload(
                                MediaRequestType.POPULAR
                            )
                        )
                    }
                )
            }
            R.id.nav_popular_movies -> {
                selectedTitle = R.string.nav_popular_movies
                actionUpFragment = FragmentMovieList.newInstance(
                    Bundle().also {
                        it.putParcelable(
                            FragmentMovieList.PARAM_MOVIE_TYPE,
                            MediaPayload(
                                MediaRequestType.POPULAR
                            )
                        )
                    }
                )
            }
        }

        bottomAppBar.setTitle(selectedTitle)
        bottomDrawerBehavior?.state = BottomSheetBehavior.STATE_HIDDEN

        try {
            actionUpFragment?.apply {
                this as Fragment
                supportFragmentManager.commit {
                    replace(R.id.contentFrame, this@apply, tag)
                }
            }
        } catch (e: Exception) {
            Timber.tag(moduleTag).e(e)
        }
    }

    override fun onUpdateUserInterface() {
        if (selectedItem != 0) launch {
            onNavigate(selectedItem)
        }
        else launch {
            onNavigate(R.id.nav_popular_series)
        }
    }

    override fun onFetchDataInitialize() {

    }

    companion object {
        private const val key_navigation_selected = "key_navigation_selected"
        private const val key_navigation_title = "key_navigation_title"
    }
}
