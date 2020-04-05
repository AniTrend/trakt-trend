package io.wax911.trakt.data.extension

import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

inline fun <reified T> getTypeToken(): Type =
    object : TypeToken<T>() {}.type