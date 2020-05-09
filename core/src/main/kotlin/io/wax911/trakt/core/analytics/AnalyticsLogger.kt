package io.wax911.trakt.core.analytics

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import co.anitrend.arch.extension.empty
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import io.fabric.sdk.android.Fabric
import io.wax911.trakt.core.BuildConfig
import timber.log.Timber

class AnalyticsLogger(
    context: Context
): Timber.Tree(), ISupportAnalytics {

    private var fabric: Fabric? = null
    private val analytics: FirebaseAnalytics =
        FirebaseAnalytics.getInstance(context).apply {
            setAnalyticsCollectionEnabled(
                true
            )
        }

    init {
        fabric = Fabric.Builder(context).kits(Crashlytics())
            .appIdentifier(BuildConfig.BUILD_TYPE)
            .build().let { Fabric.with(it) }
    }

    /**
     * Write a log message to its destination. Called for all level-specific methods by default.
     *
     * @param priority Log level. See [Log] for constants.
     * @param tag Explicit or inferred tag. May be `null`.
     * @param message Formatted log message. May be `null`, but then `t` will not be.
     * @param throwable Accompanying exceptions. May be `null`, but then `message` will not be.
     */
    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority < Log.INFO)
            return

        runCatching {
            Crashlytics.setInt(PRIORITY, priority)
            Crashlytics.setString(TAG, tag)
            Crashlytics.setString(MESSAGE, message)
        }.exceptionOrNull()?.printStackTrace()

        when (throwable) {
            null -> log(priority, tag, message)
            else -> logException(throwable)
        }
    }

    override fun logCurrentScreen(context: FragmentActivity, tag : String) {
        runCatching {
            fabric?.currentActivity = context
            analytics.setCurrentScreen(context, tag, null)
        }.exceptionOrNull()?.printStackTrace()
    }

    override fun logCurrentState(tag: String, bundle: Bundle?) {
        runCatching {
            bundle?.also { analytics.logEvent(tag, it) }
        }.exceptionOrNull()?.printStackTrace()
    }

    override fun logException(throwable: Throwable) {
        runCatching {
            Crashlytics.logException(throwable)
        }.exceptionOrNull()?.printStackTrace()
    }

    override fun log(priority: Int, tag: String?, message: String) {
        runCatching {
            Crashlytics.log(priority, tag, message)
        }.exceptionOrNull()?.printStackTrace()
    }

    override fun clearCrashAnalyticsSession() {
        runCatching {
            Crashlytics.setUserIdentifier(String.empty())
            analytics.resetAnalyticsData()
        }.exceptionOrNull()?.printStackTrace()
    }

    override fun setCrashAnalyticIdentifier(identifier: String) {
        runCatching {
            Crashlytics.setUserIdentifier(identifier)
        }.exceptionOrNull()?.printStackTrace()
    }

    companion object {
        private const val PRIORITY = "priority"
        private const val TAG = "tag"
        private const val MESSAGE = "message"
    }
}