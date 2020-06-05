import io.wax911.trakt.buildSrc.Libraries

plugins {
    id("io.wax911.trakt.plugin")
}

dependencies {
    implementation(Libraries.AndroidX.Recycler.recyclerView)
    implementation(Libraries.Coil.coil)
}