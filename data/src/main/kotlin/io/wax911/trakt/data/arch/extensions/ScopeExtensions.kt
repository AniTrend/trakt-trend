package io.wax911.trakt.data.arch.extensions

import com.uwetrottmann.tmdb2.Tmdb
import com.uwetrottmann.trakt5.TraktV2
import io.wax911.trakt.data.arch.controller.strategy.policy.OfflineStrategy
import io.wax911.trakt.data.arch.controller.strategy.policy.OnlineStrategy
import io.wax911.trakt.data.arch.database.ITraktTrendDatabase
import org.koin.core.scope.Scope

/**
 * Facade for supplying database contract
 */
internal fun Scope.db() = get<ITraktTrendDatabase>()
internal fun Scope.trakt() = get<TraktV2>()
internal fun Scope.tmdb() = get<Tmdb>()

/**
 * Facade for supplying online strategy
 */
internal fun <T> Scope.online() =
    OnlineStrategy.create<T>(
        connectivity = get()
    )

/**
 * Facade for supplying offline strategy
 */
internal fun <T> Scope.offline() =
    OfflineStrategy.create<T>()