import io.wax911.trakt.buildSrc.Libraries

plugins {
    id("io.wax911.trakt.plugin")
}

dependencies {
    implementation(project(":navigation"))
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(Libraries.AniTrend.Arch.recycler)

    implementation(Libraries.AndroidX.Core.coreKtx)
    implementation(Libraries.AndroidX.Work.runtimeKtx)
    implementation(Libraries.AndroidX.Fragment.fragmentKtx)
    implementation(Libraries.AndroidX.SwipeRefresh.swipeRefreshLayout)

    implementation(Libraries.Coil.coil)

    /** Timber Trees */
    implementation(Libraries.treessence)
}