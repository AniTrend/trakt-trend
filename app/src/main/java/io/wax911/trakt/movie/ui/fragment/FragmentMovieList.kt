package io.wax911.trakt.movie.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
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
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

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
                    Toast.makeText(target.context, "${data.second?.media?.id}", Toast.LENGTH_SHORT).show()
                }

                override fun onItemLongClick(target: View, data: Pair<Int, ISharedMediaWithImage?>) {

                }
            },
            stateConfig
        )
    }

    override fun setUpViewModelObserver() {
        viewModel.modelState.model.observe(
            viewLifecycleOwner,
            Observer {
                onPostModelChange(it)
            }
        )
    }

    override fun viewModelState() = viewModel.modelState

    override fun initializeComponents(savedInstanceState: Bundle?) {
        Timber.tag(moduleTag).v(viewModel.toString())
    }

    override fun onUpdateUserInterface() {

    }

    override fun onFetchDataInitialize() {
        pagingMediaPayload?.also {
            viewModel.modelState(
                payload = it
            )
        }
    }

    /**
     * Called when the view previously created by [onCreateView] has
     * been detached from the fragment. The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after [onStop] and before [onDestroy]. It is called
     * **regardless** of whether [onCreateView] returned a
     * non-null view. Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    override fun onDestroyView() {
        supportRecyclerView?.adapter = null
        super.onDestroyView()
    }

    companion object {
        const val PARAM_MOVIE_TYPE = ":PARAM_SHOW_TYPE"
    }
}