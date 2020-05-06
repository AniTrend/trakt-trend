package io.wax911.trakt.core.model

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Fragment loader helper
 */
data class FragmentItem<T: Fragment>(
    val parameter: Bundle,
    val fragment: Class<T>
) {
    fun tag() = fragment.simpleName
}