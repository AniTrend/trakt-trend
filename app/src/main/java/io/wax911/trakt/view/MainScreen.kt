package io.wax911.trakt.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.extension.ext.UNSAFE
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import io.wax911.trakt.R
import io.wax911.trakt.core.extension.commit
import io.wax911.trakt.core.extension.toBundle
import io.wax911.trakt.core.model.FragmentItem
import io.wax911.trakt.core.view.activity.TraktTrendActivity
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.domain.usecases.MediaRequestType
import io.wax911.trakt.navigation.NavigationTargets
import io.wax911.trakt.navigation.extensions.forFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import timber.log.Timber

class MainScreen : TraktTrendActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val bottomDrawerBehavior
            by lazy(UNSAFE) {
                BottomSheetBehavior.from(bottomNavigationDrawer)
            }

    @IdRes
    private var selectedItem: Int = R.id.nav_popular_series

    @StringRes
    private var selectedTitle: Int = R.string.nav_shows

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottomAppBar)
        bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun initializeComponents(savedInstanceState: Bundle?) {
        bottomAppBar.setNavigationOnClickListener {
            bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        bottomNavigationView.apply {
            setNavigationItemSelectedListener(this@MainScreen)
            setCheckedItem(selectedItem)
        }
        updateUserInterface()
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
        when (bottomDrawerBehavior.state) {
            BottomSheetBehavior.STATE_EXPANDED ->
                bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            BottomSheetBehavior.STATE_HALF_EXPANDED,
            BottomSheetBehavior.STATE_COLLAPSED ->
                bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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
                onNavigateToTarget(selectedItem)
            } else onNavigateToTarget(item.itemId)

        }
        return true
    }

    private fun onNavigate(@IdRes menu: Int) {
        var fragmentItem: FragmentItem<Fragment>? = null
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
                selectedTitle = R.string.nav_shows
                fragmentItem = NavigationTargets.ShowListContent
                    .forFragment()?.let {
                        FragmentItem(
                            parameter = MediaPayload(
                                MediaRequestType.POPULAR
                            ).toBundle(NavigationTargets.ShowListContent.PARAM),
                            fragment = it
                        )
                    }
            }
            R.id.nav_popular_movies -> {
                selectedTitle = R.string.nav_movies
                fragmentItem = NavigationTargets
                    .MovieListContent
                    .forFragment()?.let {
                    FragmentItem(
                        parameter = MediaPayload(
                            MediaRequestType.POPULAR
                        ).toBundle(NavigationTargets.MovieListContent.PARAM),
                        fragment = it
                    )
                }
            }
        }

        bottomAppBar.setTitle(selectedTitle)
        bottomDrawerBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        currentFragmentTag = supportFragmentManager.commit(R.id.contentFrame, fragmentItem) {
            // Nothing to do with transaction, no animations or anything like that
        }
    }

    private fun onNavigateToTarget(@IdRes menu: Int) {
        lifecycleScope.launch {
            runCatching {
                onNavigate(menu)
            }.onFailure {
                Timber.tag(moduleTag).e(it)
            }
        }
    }

    private fun updateUserInterface() {
        if (selectedItem != 0)
            onNavigateToTarget(selectedItem)
        else
            onNavigateToTarget(R.id.nav_popular_series)
    }

    companion object {
        private const val key_navigation_selected = "key_navigation_selected"
        private const val key_navigation_title = "key_navigation_title"
    }
}
