import io.wax911.trakt.buildSrc.Libraries

plugins {
    id("io.wax911.trakt.plugin")
}

dependencies {
    implementation(project(":domain"))

    implementation(Libraries.AndroidX.Collection.collectionKtx)

    implementation(Libraries.tmdb) {
        exclude(group = "org.threeten", module = "threetenbp")
    }
    implementation(Libraries.trakt) {
        exclude(group = "org.threeten", module = "threetenbp")
    }

    implementation(Libraries.threeTenBp)

    debugImplementation(Libraries.Chuncker.debug)
    releaseImplementation(Libraries.Chuncker.release)
}