package io.wax911.trakt.shared

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.anitrend.arch.ui.recycler.adapter.SupportPagedListAdapter
import co.anitrend.arch.ui.recycler.holder.SupportViewHolder
import co.anitrend.arch.ui.recycler.holder.event.ItemClickListener
import co.anitrend.arch.ui.util.StateLayoutConfig
import coil.request.RequestDisposable
import io.wax911.trakt.core.extension.using
import io.wax911.trakt.core.helpers.coil.model.ImageConfig
import io.wax911.trakt.databinding.AdapterMediaItemBinding
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage

class MediaAdapter(
    private val clickListener: ItemClickListener<ISharedMediaWithImage>,
    override val stateConfig: StateLayoutConfig
) : SupportPagedListAdapter<ISharedMediaWithImage>() {

    init {
        setHasStableIds(true)
    }

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
     * Should provide the required view holder, this function is a substitute for [onCreateViewHolder] which now
     * has extended functionality
     */
    override fun createDefaultViewHolder(
        parent: ViewGroup,
        viewType: Int,
        layoutInflater: LayoutInflater
    ): SupportViewHolder<ISharedMediaWithImage> {
        return ShowViewHolder(
            AdapterMediaItemBinding.inflate(
                layoutInflater,
                parent,
                false
            ),
            clickListener
        )
    }


    class ShowViewHolder(
        private val binding: AdapterMediaItemBinding,
        private var clickListener: ItemClickListener<ISharedMediaWithImage>?
    ): SupportViewHolder<ISharedMediaWithImage>(binding.root) {

        private var media: ISharedMediaWithImage? = null
        private var disposible: RequestDisposable? = null

        /**
         * Load images, text, buttons, etc. in this method from the given parameter
         *
         * @param model Is the liveData at the current adapter position
         */
        override fun invoke(model: ISharedMediaWithImage?) {
            media = model?.apply {
                disposible = binding.showImage.using(image)
                binding.showTitle.text = media.title
            }
            clickListener?.also { clickHandler ->
                binding.root.setOnClickListener {
                    onItemClick(it, clickHandler)
                }
            }
        }

        override fun onViewRecycled() {
            binding.root.setOnClickListener(null)
            disposible?.dispose()
            clickListener = null
            disposible = null
            media = null
        }

        override fun onItemClick(
            view: View,
            itemClickListener: ItemClickListener<ISharedMediaWithImage>
        ) {
            performClick(
                clickListener = itemClickListener,
                entity = media,
                view = view
            )
        }
    }
}