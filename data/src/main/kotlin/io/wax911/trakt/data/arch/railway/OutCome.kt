package io.wax911.trakt.data.arch.railway

/**
 * Representation of the result of an expression
 */
sealed class OutCome<T> {
    /**
     * Represent the success of an expression
     */
    internal data class Pass<T>(
        val result: T
    ) : OutCome<T>()

    /**
     * Represent the failure of an expression
     */
    internal data class Fail<T>(
        val errors: List<Throwable>
    ) : OutCome<T>()
}