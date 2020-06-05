package io.wax911.trakt.analytics

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import co.anitrend.arch.extension.ext.empty
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class AnalyticsLogger(
    private val crashlytics: FirebaseCrashlytics,
    private val analytics: FirebaseAnalytics
): Timber.Tree(), ISupportAnalytics {

    init {
        analytics.setAnalyticsCollectionEnabled(false)
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

        crashlytics.setCustomKey(PRIORITY, priority)
        crashlytics.setCustomKey(TAG, tag ?: moduleTag)
        crashlytics.setCustomKey(MESSAGE, message)

        when (throwable) {
            null -> log(priority, tag, message)
            else -> logException(throwable)
        }
    }

    override fun logCurrentScreen(context: FragmentActivity, tag : String) {
        analytics.setCurrentScreen(context, tag, null)
    }

    override fun logCurrentState(tag: String, bundle: Bundle?) {
        bundle?.also { analytics.logEvent(tag, it) }
    }

    override fun logException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    override fun log(priority: Int, tag: String?, message: String) {
        crashlytics.log(message)
    }

    override fun clearCrashAnalyticsSession() {
        crashlytics.setUserId(String.empty())
        analytics.resetAnalyticsData()
    }

    override fun setCrashAnalyticIdentifier(identifier: String) {
        crashlytics.setUserId(identifier)
    }

    companion object {
        private const val PRIORITY = "priority"
        private const val TAG = "tag"
        private const val MESSAGE = "message"

        private val moduleTag = AnalyticsLogger::class.java.simpleName
    }
}