package io.wax911.trakt.discover.show.view.screen

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.ext.extra
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import io.wax911.trakt.core.view.activity.TraktTrendActivity
import io.wax911.trakt.discover.show.R
import io.wax911.trakt.discover.show.databinding.ShowScreenBinding
import io.wax911.trakt.navigation.NavigationTargets.ShowScreen
import kotlinx.coroutines.delay

class ShowScreen : TraktTrendActivity() {

    private lateinit var binding: ShowScreenBinding

    private val params
            by extra<ShowScreen.Params>(ShowScreen.PARAM)

    /**
     * Additional initialization to be done in this method, this is called in during
     * [androidx.fragment.app.FragmentActivity.onPostCreate]
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        binding.supportStateLayout.stateConfigFlow.value = StateLayoutConfig(
            loadingDrawable = R.mipmap.ic_launcher_round,
            errorDrawable = R.mipmap.ic_launcher_round
        )
        lifecycleScope.launchWhenResumed {
            binding.supportStateLayout.networkMutableStateFlow.value =
                NetworkState.Error(
                    heading = "Seems like you clicked on an item!",
                    message = "Identifier of selected item: ${params?.showId}"
                )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ShowScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}