import io.wax911.trakt.buildSrc.Libraries

plugins {
    id("io.wax911.trakt.plugin")
}

dependencies {
    implementation(Libraries.Koin.core)
    implementation(Libraries.Koin.extension)
    implementation(Libraries.Koin.AndroidX.fragment)
    implementation(Libraries.AndroidX.Collection.collectionKtx)

    testImplementation(Libraries.Koin.test)
}