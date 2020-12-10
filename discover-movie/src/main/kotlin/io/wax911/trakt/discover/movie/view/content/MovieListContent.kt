package io.wax911.trakt.discover.movie.view.content

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.ext.argument
import co.anitrend.arch.recycler.common.DefaultClickableItem
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import io.wax911.trakt.core.view.fragment.TraktFragmentList
import io.wax911.trakt.discover.movie.R
import io.wax911.trakt.discover.movie.viewmodel.MovieViewModel
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.navigation.NavMovie
import io.wax911.trakt.shared.discover.adapter.MediaAdapter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieListContent(
    override val defaultSpanSize: Int = R.integer.grid_list_x3
) : TraktFragmentList<ISharedMediaWithImage>() {

    private val payload by argument<MediaPayload>(NavMovie.bundleKey)

    private val viewModel by viewModel<MovieViewModel>()

    /**
     * State configuration for any underlying state representing widgets
     */
    override val stateConfig: StateLayoutConfig by inject()

    /**
     * Adapter that should be used for the recycler view, by default [StateRestorationPolicy]
     * is set to [StateRestorationPolicy.PREVENT_WHEN_EMPTY]
     */
    override val supportViewAdapter by lazy {
        MediaAdapter(resources, stateConfig)
    }

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * @param savedInstanceState
     */
    @FlowPreview
    override fun initializeComponents(savedInstanceState: Bundle?) {
        super.initializeComponents(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            supportViewAdapter.clickableStateFlow.debounce(16)
                .filterIsInstance<DefaultClickableItem<ISharedMediaWithImage>>()
                .collect {
                    val model = it.data
                    Toast.makeText(
                        context,
                        "${model?.media?.id} - ${model?.media?.title}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    /**
     * Stub to trigger the loading of data, by default this is only called
     * when [supportViewAdapter] has no data in its underlying source.
     *
     * This is called when the fragment reaches it's [onStart] state
     *
     * @see initializeComponents
     */
    override fun onFetchDataInitialize() {
        payload?.also {
            viewModel.modelState(
                payload = it
            )
        }
        if (payload == null)
            supportStateLayout?.networkMutableStateFlow?.value =
                NetworkState.Error(
                    heading = "Missing payload",
                    message = "Did you forget to pass in a payload?"
                )

    }

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {
        viewModelState().model.observe(
            viewLifecycleOwner,
            ::onPostModelChange
        )
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.modelState
}