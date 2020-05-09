package io.wax911.trakt.core.extension

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import io.wax911.trakt.core.model.FragmentItem
import timber.log.Timber

/**
 * Helper extension for changing parcels to bundles
 */
fun Parcelable.toBundle(key: String) =
    Bundle().apply {
        putParcelable(key, this@toBundle)
    }

/**
 * Checks for existing fragment in [FragmentManager], if one exists that is used otherwise
 * a new instance is created.
 *
 * @return tag of the fragment
 *
 * @see androidx.fragment.app.commit
 */
inline fun FragmentManager.commit(
    @IdRes contentFrame: Int,
    fragmentItem: FragmentItem<*>?,
    action: FragmentTransaction.() -> Unit
) : String? {
    return if (fragmentItem != null) {
        val fragmentTag = fragmentItem.tag()
        val backStack = findFragmentByTag(fragmentTag)

        commit {
            action()
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
        Timber.tag(javaClass.simpleName).v("FragmentItem model is null")
        null
    }
}