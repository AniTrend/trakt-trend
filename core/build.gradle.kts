import io.wax911.trakt.buildSrc.Libraries

plugins {
    id("io.wax911.trakt.plugin")
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(Libraries.AndroidX.Core.coreKtx)
    implementation(Libraries.AndroidX.Work.runtimeKtx)
    implementation(Libraries.AndroidX.Fragment.fragmentKtx)

    implementation(Libraries.Coil.coil)
}