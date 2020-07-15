import io.wax911.trakt.buildSrc.common.Versions.smartTab

plugins {
    id("io.wax911.trakt.plugin")
}

//apply(from = "../common-app.gradle")

dependencies {
    implementation(project(":shared-discover"))

    implementation("com.ogaclejapan.smarttablayout:library:$smartTab")
    implementation("com.ogaclejapan.smarttablayout:utils-v4:$smartTab")
    implementation("com.ogaclejapan.smarttablayout:library:$smartTab")
}
