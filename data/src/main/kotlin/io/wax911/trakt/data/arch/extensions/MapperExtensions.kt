package io.wax911.trakt.data.arch.extensions

import co.anitrend.arch.extension.dispatchers.contract.ISupportDispatcher
import io.wax911.trakt.data.arch.controller.core.DefaultController
import io.wax911.trakt.data.arch.controller.strategy.contract.ControllerStrategy
import io.wax911.trakt.data.arch.mapper.DefaultMapper
import io.wax911.trakt.data.arch.network.trakt.TraktNetworkClient
import org.koin.core.scope.Scope

/**
 * Extension to help us create a controller
 */
internal fun <S, D> Scope.defaultController(
    mapper: DefaultMapper<S, D>,
    strategy: ControllerStrategy<D> = online(),
    dispatcher: ISupportDispatcher = get()
) = DefaultController(
    mapper = mapper,
    strategy = strategy,
    dispatcher = dispatcher.io,
    client = TraktNetworkClient(
        dispatcher = dispatcher.io
    )
)