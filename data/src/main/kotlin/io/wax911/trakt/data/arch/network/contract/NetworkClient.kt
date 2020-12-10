package io.wax911.trakt.data.arch.network.contract

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException


/**
 * A network client that manages how requests should be run and when to retry
 *
 * @property dispatcher A [CoroutineDispatcher] that should be used for running requests
 */
internal abstract class NetworkClient<T> {

    protected abstract val dispatcher: CoroutineDispatcher
    protected val moduleTag: String = javaClass.simpleName

    /**
     * @return [Response.body] of the response
     *
     * @throws HttpException When the request was not successful
     */
    @Throws(HttpException::class)
    protected open fun Response<T>.bodyOrThrow(): T {
        if (!isSuccessful) throw HttpException(this)
        return requireNotNull(body()) {
            "Response<T>.bodyOrThrow() -> response body was null"
        }
    }

    /**
     * @return [Boolean] whether or not the request should be retried based on the [exception] received
     */
    protected open fun defaultShouldRetry(exception: Throwable) = when (exception) {
        is HttpException -> exception.code() == 429
        is SocketTimeoutException,
        is IOException -> true
        else -> false
    }

    /**
     * Executes the request
     *
     * @param shouldRetry Conditions to determine when a request should be retried
     * @param defaultDelay Initial delay before retrying
     * @param maxAttempts Max number of attempts to retry
     */
    protected open suspend fun Call<T>.execute(
        defaultDelay: Long,
        maxAttempts: Int,
        shouldRetry: (Throwable) -> Boolean
    ): Response<T> {
        repeat(maxAttempts) { attempt ->
            var nextDelay = attempt * attempt * defaultDelay
            runCatching {
                withContext(dispatcher) { execute() }
            }.onSuccess { response ->
                return response
            }.onFailure { exception ->
                Timber.tag(moduleTag).d("Request threw an exception -> $exception")

                // The response failed, so lets see if we should retry again
                if (attempt == (maxAttempts - 1) || !shouldRetry(exception)) {
                    Timber.tag(moduleTag).w(
                        exception, "Cannot retry on exception or maximum retries reached"
                    )
                    throw exception
                }

                if (exception is HttpException) {
                    // If we have a HttpException, check whether we have a Retry-After
                    // header to decide how long to delay
                    val retryAfterHeader = exception.response()?.headers()?.get(RETRY_AFTER_KEY)
                    if (retryAfterHeader != null && retryAfterHeader.isNotEmpty()) {
                        // Got a Retry-After value, try and parse it to an long
                        Timber.tag(moduleTag).i("Rate limit reached")
                        try {
                            nextDelay = (retryAfterHeader.toLong() + 10).coerceAtLeast(defaultDelay)
                        } catch (nfe: NumberFormatException) {
                            // Probably won't happen, ignore the value and use the generated default above
                        }
                    }
                }

                Timber.tag(moduleTag).i(
                    "Retrying request in $nextDelay seconds, attempt -> $attempt/$maxAttempts"
                )
                delay(nextDelay)
            }
        }

        // We should never hit here
        throw IllegalStateException("$moduleTag -> Unrecoverable state while executing request")
    }

    /**
     * Automatically runs the suspendable operation and returns the body
     *
     * @param deferredRequest The request which needs to be executed
     * @param shouldRetry Conditions to determine when a request should be retried
     * @param firstDelay Initial delay before retrying
     * @param maxAttempts Max number of attempts to retry
     *
     * @throws HttpException When the [maxAttempts] have been exhausted, or unhandled exception
     */
    @Throws(HttpException::class)
    internal suspend fun fetch(
        deferredRequest: Call<T>,
        firstDelay: Long = 500,
        maxAttempts: Int = 3,
        shouldRetry: (Throwable) -> Boolean = ::defaultShouldRetry,
    ) = deferredRequest.execute(
        firstDelay,
        maxAttempts,
        shouldRetry
    ).bodyOrThrow()

    companion object {
        private const val RETRY_AFTER_KEY = "Retry-After"
    }
}