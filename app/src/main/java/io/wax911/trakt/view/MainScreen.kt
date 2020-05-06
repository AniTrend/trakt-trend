package io.wax911.trakt.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import io.wax911.trakt.R
import io.wax911.trakt.core.extension.commit
import io.wax911.trakt.core.view.TraktTrendActivity
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.domain.usecases.MediaRequestType
import io.wax911.trakt.core.model.FragmentItem
import io.wax911.trakt.movie.ui.fragment.FragmentMovieList
import io.wax911.trakt.show.ui.fragment.FragmentShowList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.scope.lifecycleScope

class MainScreen : TraktTrendActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val bottomDrawerBehavior: BottomSheetBehavior<FrameLayout>?
        get() = BottomSheetBehavior.from(bottomNavigationDrawer)

    @IdRes
    private var selectedItem: Int = R.id.nav_popular_series

    @StringRes
    private var selectedTitle: Int = R.string.nav_popular_series

    override fun configureActivity() {
        super.configureActivity()
        setupKoinFragmentFactory(lifecycleScope)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(bottomAppBar)
        bottomDrawerBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun initializeComponents(savedInstanceState: Bundle?) {
        bottomAppBar.apply {
            setNavigationOnClickListener {
                bottomDrawerBehavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
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
        var fragmentItem: FragmentItem<*>? = null
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
                fragmentItem = FragmentItem(
                    parameter = Bundle().apply {
                        putParcelable(
                            FragmentShowList.PARAM_SHOW_TYPE,
                            MediaPayload(
                                MediaRequestType.POPULAR
                            )
                        )
                    },
                    fragment = FragmentShowList::class.java
                )
            }
            R.id.nav_popular_movies -> {
                selectedTitle = R.string.nav_popular_movies
                fragmentItem = FragmentItem(
                    parameter = Bundle().apply {
                        putParcelable(
                            FragmentMovieList.PARAM_MOVIE_TYPE,
                            MediaPayload(
                                MediaRequestType.POPULAR
                            )
                        )
                    },
                    fragment = FragmentMovieList::class.java
                )
            }
        }

        bottomAppBar.setTitle(selectedTitle)
        bottomDrawerBehavior?.state = BottomSheetBehavior.STATE_HIDDEN

        currentFragmentTag = supportFragmentManager.commit(R.id.contentFrame, fragmentItem)
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
