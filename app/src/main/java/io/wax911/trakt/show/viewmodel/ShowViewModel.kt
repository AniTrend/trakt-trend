package io.wax911.trakt.show.viewmodel

import androidx.paging.PagedList
import co.anitrend.arch.core.viewmodel.SupportPagingViewModel
import io.wax911.trakt.data.entitiy.show.ShowEntity
import io.wax911.trakt.data.usecase.show.ShowPagedListUseCase
import io.wax911.trakt.domain.usecases.show.TraktShowUseCase

class ShowViewModel(
    override val useCase: ShowPagedListUseCase
) : SupportPagingViewModel<TraktShowUseCase.Payload, PagedList<ShowEntity>>()