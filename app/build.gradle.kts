import io.wax911.trakt.buildSrc.Libraries

plugins {
    id("io.wax911.trakt.plugin")
}

dependencies {

    implementation(Libraries.Google.Material.material)
    implementation(Libraries.Google.Firebase.Analytics.analyticsKtx)
    implementation(Libraries.Google.Firebase.Crashlytics.crashlytics)

    implementation(Libraries.AndroidX.Core.coreKtx)
    implementation(Libraries.AndroidX.Work.runtimeKtx)
    implementation(Libraries.AndroidX.Fragment.fragmentKtx)
    implementation(Libraries.AndroidX.Activity.activityKtx)
    implementation(Libraries.AndroidX.ConstraintLayout.constraintLayout)
    implementation(Libraries.AndroidX.SwipeRefresh.swipeRefreshLayout)

    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation(Libraries.Square.LeakCanary.leakCanary)
    debugImplementation(Libraries.debugDb)
}

if (file("google-services.json").exists()) {
    plugins {
        id("com.google.gms.google-services")
        id("com.google.firebase.crashlytics")
    }
}
