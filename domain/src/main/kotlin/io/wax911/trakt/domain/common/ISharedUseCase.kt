package io.wax911.trakt.domain.common

import co.anitrend.arch.domain.common.IUseCase
import co.anitrend.arch.domain.state.UiState

/**
 * Contract for paging use cases
 */
interface ISharedUseCase<P, R : UiState<*>>: IUseCase