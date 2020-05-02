package io.wax911.trakt.domain.usecases.series

import co.anitrend.arch.domain.common.IUserInterfaceState
import io.wax911.trakt.domain.common.ISharedUseCase
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.domain.repositories.series.SeriesRepository

abstract class SeriesUseCase<R: IUserInterfaceState<*>>(
    protected val repository: SeriesRepository<R>
) : ISharedUseCase<MediaPayload, R>