package io.wax911.trakt.movie.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.whenResumed
import co.anitrend.arch.extension.LAZY_MODE_UNSAFE
import co.anitrend.arch.extension.argument
import co.anitrend.arch.ui.fragment.paged.SupportFragmentPagedList
import co.anitrend.arch.ui.recycler.holder.event.ItemClickListener
import co.anitrend.arch.ui.util.StateLayoutConfig
import io.wax911.trakt.R
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.movie.viewmodel.MovieViewModel
import io.wax911.trakt.shared.MediaAdapter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentMovieList(
    override val columnSize: Int = R.integer.grid_list_x3
) : SupportFragmentPagedList<ISharedMediaWithImage>() {

    private val pagingMediaPayload
            by argument<MediaPayload>(PARAM_MOVIE_TYPE)

    override val stateConfig by inject<StateLayoutConfig>()

    private val viewModel by viewModel<MovieViewModel>()

    override val supportViewAdapter by lazy(LAZY_MODE_UNSAFE) {
        MediaAdapter(
            object : ItemClickListener<ISharedMediaWithImage> {
                override fun onItemClick(target: View, data: Pair<Int, ISharedMediaWithImage?>) {

                }

                override fun onItemLongClick(target: View, data: Pair<Int, ISharedMediaWithImage?>) {

                }
            },
            stateConfig
        )
    }

    /**
     * Invoke view model observer to watch for changes
     */
    override fun setUpViewModelObserver() {
        viewModel.modelState.model.observe(this, Observer {
            onPostModelChange(it)
        })
    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState() = viewModel.modelState

    /**
     * Additional initialization to be done in this method, if the overriding class is type of [SupportFragmentPagedList]
     * then this method will be called in [SupportFragmentPagedList.onCreate].
     * invokes this function
     *
     * @see [SupportFragmentPagedList.onCreate]
     * @param
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        launch {
            viewLifecycleOwner.whenResumed {
                onFetchDataInitialize()
            }
        }
    }

    /**
     * Update views or bind a liveData to them
     */
    override fun onUpdateUserInterface() {

    }

    override fun onFetchDataInitialize() {
        pagingMediaPayload?.also {
            viewModel.modelState(
                payload = it
            )
        }
    }

    companion object {

        const val PARAM_MOVIE_TYPE = "FragmentMovieList:PARAM_MOVIE_TYPE"

        fun newInstance(bundle: Bundle?): FragmentMovieList {
            val fragment = FragmentMovieList()
            fragment.arguments = bundle
            return fragment
        }
    }
}