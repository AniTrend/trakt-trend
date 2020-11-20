package io.wax911.trakt.koin

import android.content.Context
import android.net.ConnectivityManager
import co.anitrend.arch.core.analytic.contract.ISupportAnalytics
import co.anitrend.arch.core.provider.SupportFileProvider
import co.anitrend.arch.extension.ext.systemServiceOf
import co.anitrend.arch.extension.network.SupportConnectivity
import co.anitrend.arch.ui.view.widget.model.StateLayoutConfig
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import io.wax911.trakt.R
import io.wax911.trakt.analytics.AnalyticsLogger
import io.wax911.trakt.core.koin.helper.DynamicFeatureModuleHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

private val appModule = module {
    factory<ISupportAnalytics> {
        AnalyticsLogger(
            crashlytics = FirebaseCrashlytics.getInstance(),
            analytics = Firebase.analytics
        )
    }
    factory {
        SupportFileProvider()
    }
    single {
        StateLayoutConfig(
            loadingDrawable = null,
            errorDrawable = R.drawable.ic_support_empty_state,
            loadingMessage = null,
            retryAction = R.string.action_retry
        )
    }
    single {
        SupportConnectivity(
            androidApplication().systemServiceOf<ConnectivityManager>(
                Context.CONNECTIVITY_SERVICE
            )
        )
    }
}


internal val appModules by lazy {
    DynamicFeatureModuleHelper(listOf(appModule))
}