package io.wax911.trakt.data.arch.database.extensions

import co.anitrend.arch.extension.empty

internal fun List<*>.toCommaSeparatedValues(): String {
    return if (isNotEmpty()) {
        joinToString(separator = ",")
    } else
        String.empty()
}

internal fun String.fromCommaSeparatedValues(): List<String> {
    return if (isNotBlank())
        split(',')
    else
        emptyList()
}

internal inline fun <reified T: Enum<*>> String.toEnum(): T {
    val `class` = T::class.java
    return `class`.enumConstants?.first { it.name == this }!!
}

internal fun Enum<*>.fromEnum(): String {
    return name
}