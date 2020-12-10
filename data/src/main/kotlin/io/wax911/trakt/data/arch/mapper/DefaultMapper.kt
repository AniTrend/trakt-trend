package io.wax911.trakt.data.arch.mapper

import co.anitrend.arch.data.mapper.SupportResponseMapper
import io.wax911.trakt.data.arch.railway.OutCome
import timber.log.Timber

/**
 * Making it easier for us to implement error logging and provide better error messages
 */
internal abstract class DefaultMapper<S, D> : SupportResponseMapper<S, D>() {

    /**
     * Handles the persistence of [data] into a local source
     *
     * @return [OutCome.Pass] or [OutCome.Fail] of the operation
     */
    protected open suspend fun persistChanges(data: D): OutCome<Nothing?> {
        // optional
        return OutCome.Pass(null)
    }

    /**
     * If [data] is a type of [Collection], checks if empty otherwise checks nullability
     */
    protected fun <T> checkValidity(data: T?): OutCome<T> {
        if (data == null)
            return OutCome.Fail(
                listOf(Throwable("Data validity failed data is null"))
            )

        if (data is Collection<*> && data.isEmpty())
            return OutCome.Fail(
                listOf(Throwable("Data validity failed collection is empty"))
            )

        return OutCome.Pass(data)
    }

    /**
     * Logs [exceptions] of failed operations
     */
    protected fun handleException(exceptions: List<Throwable>) {
        exceptions.forEach {
            Timber.tag(moduleTag).w(it, "Unhandled exception thrown")
        }
    }
}