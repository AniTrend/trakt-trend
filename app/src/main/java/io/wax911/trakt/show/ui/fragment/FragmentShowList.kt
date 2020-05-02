package io.wax911.trakt.show.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.whenResumed
import co.anitrend.arch.domain.entities.NetworkState
import co.anitrend.arch.extension.LAZY_MODE_UNSAFE
import co.anitrend.arch.extension.argument
import co.anitrend.arch.ui.fragment.paged.SupportFragmentPagedList
import co.anitrend.arch.ui.recycler.holder.event.ItemClickListener
import co.anitrend.arch.ui.util.StateLayoutConfig
import io.wax911.trakt.R
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.shared.MediaAdapter
import io.wax911.trakt.show.viewmodel.ShowViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentShowList(
    override val columnSize: Int = R.integer.grid_list_x3
) : SupportFragmentPagedList<ISharedMediaWithImage>() {

    private val pagingMediaPayload
            by argument<MediaPayload>(PARAM_SHOW_TYPE)

    // we could inject this as a singleton through DI
    override val stateConfig by inject<StateLayoutConfig>()

    private val viewModel by viewModel<ShowViewModel>()

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
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {
        launch {
            viewLifecycleOwner.whenResumed {
                onFetchDataInitialize()
            }
        }
    }

    /**
     * Handles the updating, binding, creation or state change, depending on the context of views.
     *
     * **N.B.** Where this is called is up to the developer
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

        const val PARAM_SHOW_TYPE = "FragmentShowList:PARAM_SHOW_TYPE"

        fun newInstance(bundle: Bundle?): FragmentShowList {
            val fragment = FragmentShowList()
            fragment.arguments = bundle
            return fragment
        }
    }
}