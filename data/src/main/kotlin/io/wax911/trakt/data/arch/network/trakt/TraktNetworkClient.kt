package io.wax911.trakt.data.arch.network.trakt

import io.wax911.trakt.data.arch.network.contract.NetworkClient
import kotlinx.coroutines.CoroutineDispatcher

internal class TraktNetworkClient<R>(
    override val dispatcher: CoroutineDispatcher
) : NetworkClient<R>()