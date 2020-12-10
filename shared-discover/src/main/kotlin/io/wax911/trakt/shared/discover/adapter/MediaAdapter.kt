package io.wax911.trakt.shared.discover.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.core.model.IStateLayoutConfig
import co.anitrend.arch.recycler.action.contract.ISupportSelectionMode
import co.anitrend.arch.recycler.adapter.SupportPagedListAdapter
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import co.anitrend.arch.recycler.shared.SupportFooterLoadingItem
import co.anitrend.arch.theme.animator.ScaleAnimator
import co.anitrend.arch.theme.animator.contract.AbstractAnimator
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.shared.discover.model.MediaItem

class MediaAdapter(
    override val resources: Resources,
    override val stateConfiguration: IStateLayoutConfig,
    override var supportAction: ISupportSelectionMode<Long>? = null,
    override var customSupportAnimator: AbstractAnimator? = ScaleAnimator(),
    override val mapper: (ISharedMediaWithImage?) -> IRecyclerItem = {
        if (it != null) MediaItem(it) else SupportFooterLoadingItem(stateConfiguration)
    }
) : SupportPagedListAdapter<ISharedMediaWithImage>(MediaItem.DIFFER) {

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