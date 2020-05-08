package io.wax911.trakt.shared.discover.model

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import co.anitrend.arch.recycler.common.ClickableItem
import co.anitrend.arch.recycler.common.DefaultClickableItem
import co.anitrend.arch.recycler.holder.SupportViewHolder
import co.anitrend.arch.recycler.model.RecyclerItem
import co.anitrend.arch.recycler.model.contract.IRecyclerItem
import coil.request.RequestDisposable
import io.wax911.trakt.core.extension.using
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.shared.discover.R
import io.wax911.trakt.shared.discover.databinding.AdapterMediaItemBinding

data class MediaItem(
    val entity: ISharedMediaWithImage
) : RecyclerItem(entity.media.id.toLong()) {

    private var disposable: RequestDisposable? = null

    /**
     * Called when the [view] needs to be setup, this could be to set click listeners,
     * assign text, load images, e.t.c
     */
    override fun bind(
        view: View,
        position: Int,
        payloads: List<Any>,
        clickObservable: MutableLiveData<ClickableItem>
    ) {
        val binding = AdapterMediaItemBinding.bind(view)
        disposable = binding?.showImage?.using(entity.image)
        binding?.showTitle?.text = entity.media.title
        view.setOnClickListener {
            clickObservable.postValue(
                DefaultClickableItem(
                    data = entity,
                    view = view
                )
            )
        }
    }

    /**
     * Called when the view needs to be recycled for reuse, clear any held references
     * to objects, stop any asynchronous work, e.t.c
     */
    override fun unbind(view: View) {
        view.setOnClickListener(null)
        disposable?.dispose()
        disposable = null
    }

    /**
     * Provides a preferred span size for the item
     *
     * @param spanCount current span count which may also be [INVALID_SPAN_COUNT]
     * @param position position of the current item
     * @param resources optionally useful for dynamic size check with different configurations
     */
    override fun getSpanSize(spanCount: Int, position: Int, resources: Resources) =
        resources.getInteger(R.integer.grid_list_x3)

    companion object {

        internal fun createViewHolder(
            viewGroup: ViewGroup,
            layoutInflater: LayoutInflater
        ) = AdapterMediaItemBinding.inflate(
            layoutInflater, viewGroup, false
        ).let { SupportViewHolder(it.root) }

        internal val DIFFER = object : DiffUtil.ItemCallback<IRecyclerItem>() {
            override fun areItemsTheSame(
                oldItem: IRecyclerItem,
                newItem: IRecyclerItem
            ): Boolean {
                oldItem as MediaItem
                return oldItem.entity.media.id == oldItem.entity.media.id
            }

            override fun areContentsTheSame(
                oldItem: IRecyclerItem,
                newItem: IRecyclerItem
            ): Boolean {
                oldItem as MediaItem
                return oldItem.entity.media.hashCode() == oldItem.entity.media.hashCode()
            }
        }
    }
}