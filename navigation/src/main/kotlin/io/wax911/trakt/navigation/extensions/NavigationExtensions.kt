/*
 * Copyright (C) 2020  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.wax911.trakt.navigation.extensions

import android.content.Intent
import androidx.collection.lruCache
import androidx.fragment.app.Fragment
import io.wax911.trakt.navigation.contract.NavigationComponent
import timber.log.Timber


private const val APPLICATION_PACKAGE_NAME = "io.wax911.trakt"

private val classMap = lruCache<String, Class<*>>(
    maxSize = 5, create = { `class` ->
        Class.forName(`class`)
    }
)

private inline fun <reified T : Any> Any.castOrNull() = this as? T

private fun forIntent(className: String): Intent =
    Intent(Intent.ACTION_VIEW)
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        .setClassName(APPLICATION_PACKAGE_NAME, className)

internal fun String.loadIntentOrNull(): Intent? =
    try {
        Class.forName(this).run {
            forIntent(this@loadIntentOrNull)
        }
    } catch (e: ClassNotFoundException) {
        Timber.tag("loadIntentOrNull").e(e)
        null
    }

@Throws(ClassNotFoundException::class)
internal fun <T> String.loadClassOrNull(): Class<out T>? =
    classMap.get(this)?.castOrNull()

internal fun <T : Fragment> String.loadFragmentOrNull(): T? =
    try {
        loadClassOrNull<T>()?.newInstance()
    } catch (e: ClassNotFoundException) {
        Timber.tag("loadFragmentOrNull").e(e)
        null
    }

fun NavigationComponent.forFragment(): Class<out Fragment> =
    feature.content()