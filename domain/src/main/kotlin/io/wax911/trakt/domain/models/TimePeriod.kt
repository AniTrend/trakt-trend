package io.wax911.trakt.domain.models

enum class TimePeriod(val key: String) {
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    YEARLY("yearly"),
    ALL("all")
}