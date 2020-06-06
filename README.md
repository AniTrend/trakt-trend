# [ :biohazard: W.I.P :biohazard: ] Trakt-Trend

### Getting started

Add a file called `secrets.properties` in `./data/.config/` the file should contain the following (case sensitive):

> ```gradle
> clientId = "YOUR_OWN_TRAKT_API_KEY"
> clientSecret = "YOUR_OWN_TRAKT_API_SECRET"
> tmdbSecret = "YOUR_OWN_TMDB_API_KEY"
> ```

### The sample

Trakt-Trend is a simple application built on top of [support-arch](https://github.com/AniTrend/support-arch) only for the sole purpose
of demonstrating how to use the library in preparation for AniTrend v2.0. As such this may never become a fully featured application,
beyond being a playground.

<img src="./screenshots/Screenshot_1589033578.webp" width="250px" />&nbsp;<img src="./screenshots/Screenshot_1589033589.webp" width="250px" />

<img src="./screenshots/Screenshot_1589033594.webp" width="250px" />&nbsp;<img src="./screenshots/Screenshot_1589033600.webp" width="250px" />

### Libraries used

#### Core

- [Android KTX](https://developer.android.com/kotlin/ktx.html/)
- [Data Binding](https://developer.android.com/topic/libraries/data-binding/)
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html/)
- [Kotlin](https://kotlinlang.org/)
- [Material Components](https://material.io/develop/android/docs/getting-started/)
- [Live Data](https://developer.android.com/topic/libraries/architecture/livedata/)
- [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle/)
- [Room](https://developer.android.com/topic/libraries/architecture/room)
- [Paging](https://developer.android.com/topic/libraries/architecture/paging/)

#### Analytics

- [Firebase](https://firebase.google.com/)
- [Crashlytics](https://fabric.io/kits/android/crashlytics/)

#### Networking

- [Coil](https://coil-kt.github.io/coil/)
- [Retrofit](https://square.github.io/retrofit/)
- [trakt-java](https://github.com/JakeWharton/trakt-java)
- [tmdb-java](https://github.com/UweTrottmann/tmdb-java)


#### Dependency Injection

- [Koin](https://insert-koin.io/)

#### Testing

- [Mockk](https://mockk.io/)
- [Junit](https://developer.android.com/training/testing/junit-rules/)
- [Runner](https://developer.android.com/training/testing/junit-runner.html/)
- [Espresso](https://developer.android.com/training/testing/espresso/index.html/)

#### Logging

- [Chunker](https://github.com/ChuckerTeam/chucker)
- [Timber](https://github.com/JakeWharton/timber/)
- [Treessence](https://github.com/bastienpaulfr/Treessence)
- [OkHttp](https://square.github.io/okhttp/)

#### Acknowledgements

The [tivi](https://github.com/chrisbanes/tivi) repository has offered a lot of insights from the use
of coil, tmdb-java, trakt-java and a couple of other helpful extension functions when working with retrofit.
