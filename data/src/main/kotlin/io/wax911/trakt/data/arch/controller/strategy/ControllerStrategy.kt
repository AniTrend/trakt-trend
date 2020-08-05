package io.wax911.trakt.data.arch.controller.strategy

import androidx.lifecycle.MutableLiveData
import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.domain.entities.NetworkState

internal abstract class ControllerStrategy<D> {

    protected val moduleTag = javaClass.simpleName

    /**
     * Execute a paging task under an implementation strategy
     *
     * @param block What will be executed
     * @param requestCallback Event emitter
     */
    internal abstract suspend operator fun invoke(
        requestCallback: RequestCallback,
        block: suspend () -> D?
    ): D?

    /**
     * Execute a task under an implementation strategy
     *
     * @param block what will be executed
     * @param networkState network state event emitter
     */
    @Deprecated("Use RequestCallback instead")
    internal abstract suspend operator fun invoke(
        networkState: MutableLiveData<NetworkState>,
        block: suspend () -> D?
    ): D?
}