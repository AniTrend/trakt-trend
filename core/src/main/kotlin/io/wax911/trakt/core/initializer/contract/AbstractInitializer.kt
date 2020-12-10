package io.wax911.trakt.core.initializer.contract

import androidx.startup.Initializer

/**
 * Contract for initializer
 */
abstract class AbstractInitializer<T> : Initializer<T> {

    /**
     * @return A list of dependencies that this [Initializer] depends on. This is
     * used to determine initialization order of [Initializer]s.
     *
     * For e.g. if a [Initializer] `B` defines another [Initializer] `A` as its dependency,
     * then `A` gets initialized before `B`.
     */
    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}