package io.wax911.trakt.data.show.source.contract

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import io.wax911.trakt.data.arch.common.TraktPagedSource
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage

internal abstract class ShowPagedSource(
    dispatchers: SupportDispatchers
) : TraktPagedSource<ISharedMediaWithImage>(dispatchers) {
    abstract operator fun invoke(): LiveData<PagedList<ISharedMediaWithImage>>
}