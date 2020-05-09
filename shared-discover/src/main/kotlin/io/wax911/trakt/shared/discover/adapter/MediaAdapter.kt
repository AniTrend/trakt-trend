package io.wax911.trakt.shared.discover.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.adapter.SupportPagedListAdapter
import co.anitrend.arch.recycler.adapter.contract.ISupportAdapter.Companion.FULL_SPAN_SIZE
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.recycler.model.contract.IRecyclerItemSpan.Companion.INVALID_SPAN_COUNT
import co.anitrend.arch.theme.animator.ScaleAnimator
import co.anitrend.arch.theme.animator.contract.ISupportAnimator
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.shared.discover.model.MediaItem
import timber.log.Timber

class MediaAdapter(
    override val resources: Resources,
    override val stateConfiguration: IStateLayoutConfig,
    override var customSupportAnimator: ISupportAnimator? = ScaleAnimator(),
    override val mapper: (ISharedMediaWithImage?) -> IRecyclerItem = { MediaItem(it) }
) : SupportPagedListAdapter<ISharedMediaWithImage>(MediaItem.DIFFER) {

    /**
     * Assigned if the current adapter supports needs to supports action mode
     */
    override var supportAction: ISupportSelectionMode<Long>? = null

    /**
     * Used to get stable ids for [androidx.recyclerview.widget.RecyclerView.Adapter] but only if
     * [androidx.recyclerview.widget.RecyclerView.Adapter.setHasStableIds] is set to true.
     *
     * The identifiable id of each item should unique, and if non exists
     * then this function should return [androidx.recyclerview.widget.RecyclerView.NO_ID]
     */
    override fun getStableIdFor(item: ISharedMediaWithImage?): Long {
        return item?.media?.id?.toLong() ?: RecyclerView.NO_ID
    }

    /**
     * Should provide the required view holder, this function is a substitute for
     * [androidx.recyclerview.widget.RecyclerView.Adapter.onCreateViewHolder] which now
     * has extended functionality
     */
    override fun createDefaultViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ) = MediaItem.createViewHolder(parent, layoutInflater)
}