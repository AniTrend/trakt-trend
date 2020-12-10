package io.wax911.trakt.data.arch.controller.strategy.contract

import co.anitrend.arch.data.request.callback.RequestCallback
import co.anitrend.arch.data.request.error.RequestError
import retrofit2.HttpException
import java.net.SocketTimeoutException

internal abstract class ControllerStrategy<D> {

    protected val moduleTag: String = javaClass.simpleName

    /**
     * Creates human readable exceptions from a given exception
     */
    protected fun Throwable.generateForError() = when (this) {
        is SocketTimeoutException -> {
            RequestError(
                "Connection timed out",
                "Please check your internet connection and try again",
                cause
            )
        }
        is HttpException -> {
            RequestError(
                "Server error occurred",
                message,
                cause
            )
        }
        else -> RequestError(
            "Unknown error occurred",
            message,
            cause
        )
    }

    /**
     * Execute a task under an implementation strategy
     *
     * @param callback event emitter
     * @param block what will be executed
     */
    internal abstract suspend operator fun invoke(
        callback: RequestCallback,
        block: suspend () -> D?
    ): D?
}