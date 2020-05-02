package io.wax911.trakt.domain.usecases

enum class MediaRequestType(val authenticated: Boolean) {
    ANTICIPATED(false),
    COLLECTED(true),
    PLAYED(true),
    POPULAR(false),
    TRENDING(false),
    UPDATED(false),
    WATCHED(false)
}