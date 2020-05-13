package io.wax911.trakt.discover.show.view.screen

import android.graphics.Rect
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.extra
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import io.wax911.trakt.discover.show.R
import io.wax911.trakt.discover.show.databinding.ShowScreenBinding
import io.wax911.trakt.navigation.NavigationTargets.ShowScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import me.saket.inboxrecyclerview.PullCollapsibleActivity

class ShowScreen : PullCollapsibleActivity(), CoroutineScope by MainScope() {

    private lateinit var binding: ShowScreenBinding

    private val params by extra<ShowScreen.Params>(ShowScreen.PARAM)

    /**
     * Can be used to configure custom theme styling as desired
     */
    private fun configureActivity() {

    }

    /**
     * Additional initialization to be done in this method, this is called in during
     * [androidx.fragment.app.FragmentActivity.onPostCreate]
     *
     * @param savedInstanceState
     */
    private fun initializeComponents(savedInstanceState: Bundle?) {
        binding.supportStateLayout.stateConfig = StateLayoutConfig(
            loadingDrawable = R.mipmap.ic_launcher_round,
            errorDrawable = R.mipmap.ic_launcher_round
        )
        lifecycleScope.launchWhenResumed {
            delay(500)
            binding.supportStateLayout.networkStateLiveData.postValue(
                NetworkState.Error(
                    heading = "Seems like you clicked on an item!",
                    message = "Identifier of selected item: ${params?.showId}"
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        configureActivity()
        super.onCreate(savedInstanceState)
        binding = ShowScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        (params)?.apply {
            val (left, top, right, bottom) = bounds
            expandFrom(
                Rect(left, top, right, bottom)
            )
        } ?: expandFromTop()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initializeComponents(savedInstanceState)
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}