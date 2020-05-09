package io.wax911.trakt.splash.ui.activity

import android.os.Bundle
import co.anitrend.arch.extension.startNewActivity
import co.anitrend.arch.extension.util.date.SupportDateHelper
import io.wax911.trakt.R
import io.wax911.trakt.core.presenter.CorePresenter
import io.wax911.trakt.core.view.TraktTrendActivity
import io.wax911.trakt.view.MainScreen
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class SplashScreen : TraktTrendActivity() {

    private val supportDateHelper by inject<SupportDateHelper>()

    /**
     * Additional initialization to be done in this method, if the overriding class is type of
     * [androidx.fragment.app.Fragment] then this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate]. Otherwise
     * [androidx.fragment.app.FragmentActivity.onPostCreate] invokes this function
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        val description = getString(
            R.string.app_splash_description,
            supportDateHelper.getCurrentYear(0)
        )
        splash_description.text = description
        updateUserInterface()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    private fun updateUserInterface() {
        launch {
            delay(500)
            startNewActivity<MainScreen>(intent.extras)
            finish()
        }

    }
}
