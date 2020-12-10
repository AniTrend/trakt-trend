package io.wax911.trakt.data.arch.railway.extension

import io.wax911.trakt.data.arch.railway.OutCome

suspend infix fun <T,U> OutCome<T>.then(
    spec: suspend (T) -> OutCome<U>
) = when (this) {
    is OutCome.Pass -> spec(result)
    is OutCome.Fail -> OutCome.Fail(errors)
}

suspend infix fun <T,U> T.evaluate(
    spec: suspend (T) -> OutCome<U>
) = OutCome.Pass(this) then spec

suspend infix fun <T> OutCome<T>.otherwise(
    spec: suspend (List<Throwable>) -> Unit
) = when (this) {
    is OutCome.Fail -> spec(errors)
    else -> Unit
}