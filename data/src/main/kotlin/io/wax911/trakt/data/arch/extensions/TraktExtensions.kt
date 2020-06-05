package io.wax911.trakt.data.arch.extensions

import co.anitrend.arch.extension.dispatchers.SupportDispatchers
import co.anitrend.arch.extension.network.SupportConnectivity
import com.uwetrottmann.tmdb2.Tmdb
import com.uwetrottmann.trakt5.TraktV2
import io.wax911.trakt.data.arch.controller.TraktTrendController
import io.wax911.trakt.data.arch.controller.policy.OfflineControllerPolicy
import io.wax911.trakt.data.arch.controller.policy.OnlineControllerPolicy
import io.wax911.trakt.data.arch.controller.strategy.ControllerStrategy
import io.wax911.trakt.data.arch.database.ITraktTrendDatabase
import io.wax911.trakt.data.arch.mapper.TraktTrendMapper
import kotlinx.coroutines.delay
import org.koin.core.scope.Scope
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


internal fun <T> Response<T>.bodyOrThrow(): T {
    if (!isSuccessful) throw HttpException(this)
    return body()!!
}

private suspend inline fun <T> Call<T>.executeWithRetry(
    defaultDelay: Long = 100,
    maxAttempts: Int = 3,
    shouldRetry: (Exception) -> Boolean = ::defaultShouldRetry
): Response<T> {
    repeat(maxAttempts) { attempt ->
        var nextDelay = attempt * attempt * defaultDelay

        try {
            // Clone a new ready call if needed
            val call = if (isExecuted) clone() else this
            return call.execute()
        } catch (e: Exception) {
            // The response failed, so lets see if we should retry again
            if (attempt == (maxAttempts - 1) || !shouldRetry(e)) {
                throw e
            }

            if (e is HttpException) {
                // If we have a HttpException, check whether we have a Retry-After
                // header to decide how long to delay
                val retryAfterHeader = e.response()?.headers()?.get("Retry-After")
                if (retryAfterHeader != null && retryAfterHeader.isNotEmpty()) {
                    // Got a Retry-After value, try and parse it to an long
                    try {
                        nextDelay = (retryAfterHeader.toLong() + 10).coerceAtLeast(defaultDelay)
                    } catch (nfe: NumberFormatException) {
                        // Probably won't happen, ignore the value and use the generated default above
                    }
                }
            }
        }

        delay(nextDelay)
    }

    // We should never hit here
    throw IllegalStateException("Unknown exception from executeWithRetry")
}

internal suspend inline fun <T> Call<T>.fetchBodyWithRetry(
    firstDelay: Long = 100,
    maxAttempts: Int = 3,
    shouldRetry: (Exception) -> Boolean = ::defaultShouldRetry
) = executeWithRetry(firstDelay, maxAttempts, shouldRetry).bodyOrThrow()

private fun defaultShouldRetry(exception: Exception) = when (exception) {
    is HttpException -> exception.code() == 429
    is IOException -> true
    else -> false
}

/**
 * Extension to help us create a controller from a a mapper instance
 */
internal fun <S, D> TraktTrendMapper<S, D>.controller(
    strategy: ControllerStrategy<D>,
    supportDispatchers: SupportDispatchers
) = TraktTrendController.newInstance(
    responseMapper = this,
    strategy = strategy,
    supportDispatchers = supportDispatchers
)

internal fun Scope.trakt() = get<TraktV2>()
internal fun Scope.tmdb() = get<Tmdb>()
internal fun Scope.db() = get<ITraktTrendDatabase>()