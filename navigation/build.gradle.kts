import io.wax911.trakt.buildSrc.Libraries

plugins {
    id("io.wax911.trakt.plugin")
}

dependencies {
    implementation(Libraries.AndroidX.Collection.collectionKtx)
}