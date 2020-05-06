package io.wax911.trakt.show.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import co.anitrend.arch.extension.LAZY_MODE_UNSAFE
import co.anitrend.arch.extension.argument
import co.anitrend.arch.ui.fragment.paged.SupportFragmentPagedList
import co.anitrend.arch.ui.recycler.SupportRecyclerView
import co.anitrend.arch.ui.recycler.holder.event.ItemClickListener
import co.anitrend.arch.ui.util.StateLayoutConfig
import io.wax911.trakt.R
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.shared.MediaAdapter
import io.wax911.trakt.show.viewmodel.ShowViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentShowList(
    override val columnSize: Int = R.integer.grid_list_x3
) : SupportFragmentPagedList<ISharedMediaWithImage>() {

    private val pagingMediaPayload
            by argument<MediaPayload>(PARAM_SHOW_TYPE)

    override val stateConfig by inject<StateLayoutConfig>()

    private val viewModel by viewModel<ShowViewModel>()

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
        ).apply { stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY }
    }

    override fun setRecyclerAdapter(recyclerView: SupportRecyclerView) {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = supportViewAdapter.let {
                if (it.supportAction == null)
                    it.supportAction = supportAction

                MergeAdapter(
                    MergeAdapter.Config.Builder()
                        .setIsolateViewTypes(true)
                        .setStableIdMode(
                            MergeAdapter.Config.StableIdMode.ISOLATED_STABLE_IDS
                        ).build(),
                    it
                )
            }
        }
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
        const val PARAM_SHOW_TYPE = ":PARAM_SHOW_TYPE"
    }
}