package io.wax911.trakt.discover.show.view.content

import android.os.Bundle
import android.view.View
import co.anitrend.arch.ui.fragment.SupportFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import io.wax911.trakt.core.extension.toBundle
import io.wax911.trakt.discover.show.R
import io.wax911.trakt.discover.show.databinding.ShowContentRootBinding
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.domain.usecases.MediaRequestType
import io.wax911.trakt.navigation.NavigationTargets
import io.wax911.trakt.navigation.extensions.forFragment
import kotlinx.android.synthetic.main.custom_smart_tab.view.*

class ShowListRootContent(
    override val inflateLayout: Int = R.layout.show_content_root
) : SupportFragment() {

    private lateinit var binding: ShowContentRootBinding

    /**
     * Invoke view model observer to watch for changes, this will be called
     * called in [onViewCreated]
     */
    override fun setUpViewModelObserver() {

    }

    /**
     * Proxy for a view model state if one exists
     */
    override fun viewModelState(): Nothing? = null

    /**
     * Additional initialization to be done in this method, this method will be called in
     * [androidx.fragment.app.FragmentActivity.onCreate].
     *
     * @param savedInstanceState
     */
    override fun initializeComponents(savedInstanceState: Bundle?) {

    }

    /**
     * Called immediately after [onCreateView] has returned, but before any saved state has been
     * restored in to the view. This gives subclasses a chance to initialize themselves once
     * they know their view hierarchy has been completely created.
     *
     * The fragment's view hierarchy is not however attached to its parent at this point.
     *
     * @param view The View returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     * saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ShowContentRootBinding.bind(view)
        binding.viewPager.adapter = FragmentPagerItemAdapter(
            childFragmentManager,
            FragmentPagerItems.with(view.context).add(
                "Popular",
                NavigationTargets.ShowListContent.forFragment(),
                MediaPayload(MediaRequestType.POPULAR)
                    .toBundle(NavigationTargets.ShowListContent.PARAM)
            ).add(
                "Trending",
                NavigationTargets.ShowListContent.forFragment(),
                MediaPayload(MediaRequestType.TRENDING)
                    .toBundle(NavigationTargets.ShowListContent.PARAM)
            ).create()
        )
        view.smartTabView.setViewPager(binding.viewPager)
    }
}