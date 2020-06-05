import io.wax911.trakt.buildSrc.Libraries

plugins {
    id("io.wax911.trakt.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

dependencies {

    implementation(Libraries.Google.Material.material)
    implementation(Libraries.Google.Firebase.Analytics.analyticsKtx)
    implementation(Libraries.Google.Firebase.Crashlytics.crashlytics)

    implementation(Libraries.AndroidX.Core.coreKtx)
    implementation(Libraries.AndroidX.Work.runtimeKtx)
    implementation(Libraries.AndroidX.Fragment.fragmentKtx)

    /** Timber Trees */
    implementation(Libraries.treessence)

    // debugImplementation because LeakCanary should only run in debug builds.
    debugImplementation(Libraries.Square.LeakCanary.leakCanary)
    debugImplementation(Libraries.treessence)
}

if (file("google-services.json").exists()) {
    plugins {
        id("com.google.gms.google-services")
        id("com.google.firebase.crashlytics")
    }
}
