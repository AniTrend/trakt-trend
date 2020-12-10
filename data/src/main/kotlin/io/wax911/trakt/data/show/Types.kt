package io.wax911.trakt.data.show

import androidx.paging.PagedList
import co.anitrend.arch.data.state.DataState
import com.uwetrottmann.trakt5.entities.Show
import com.uwetrottmann.trakt5.services.Shows
import io.wax911.trakt.data.arch.controller.core.DefaultController
import io.wax911.trakt.data.show.entity.ShowEntity
import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.repositories.series.SeriesRepository
import io.wax911.trakt.domain.usecases.series.SeriesUseCase

internal typealias ShowRemoteSource = Shows

internal typealias PopularShowController = DefaultController<List<Show>, List<ShowEntity>>

internal typealias SeriesPageRepository = SeriesRepository<DataState<PagedList<ISharedMediaWithImage>>>

typealias SeriesUseCaseType = SeriesUseCase<DataState<PagedList<ISharedMediaWithImage>>>