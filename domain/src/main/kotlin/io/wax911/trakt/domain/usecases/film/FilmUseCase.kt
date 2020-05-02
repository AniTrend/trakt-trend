package io.wax911.trakt.domain.usecases.film

import co.anitrend.arch.domain.common.IUserInterfaceState
import io.wax911.trakt.domain.common.ISharedUseCase
import io.wax911.trakt.domain.models.MediaPayload
import io.wax911.trakt.domain.repositories.film.FilmRepository

abstract class FilmUseCase<R: IUserInterfaceState<*>>(
    protected val repository: FilmRepository<R>
) : ISharedUseCase<MediaPayload, R>