package io.wax911.trakt.core.extension

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import io.wax911.trakt.core.model.FragmentItem
import timber.log.Timber


private const val moduleTag = "CoreExt"

/**
 * Checks for existing fragment in [FragmentManager], if one exists that is used otherwise
 * a new instance is created.
 *
 * @return tag of the fragment
 *
 * @see androidx.fragment.app.commit
 */
fun FragmentManager.commit(
    @IdRes contentFrame: Int,
    fragmentItem: FragmentItem<*>?
) : String? {
    return if (fragmentItem != null) {
        val fragmentTag = fragmentItem.tag()
        val backStack = findFragmentByTag(fragmentTag)

        commit {
            backStack?.let {
                replace(contentFrame, it, fragmentTag)
            } ?: replace(
                contentFrame,
                fragmentItem.fragment,
                fragmentItem.parameter,
                fragmentTag
            )
        }
        fragmentTag
    } else {
        Timber.tag(moduleTag).v("FragmentItem model is null")
        null
    }
}