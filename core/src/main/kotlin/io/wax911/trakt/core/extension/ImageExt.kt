package io.wax911.trakt.core.extension

import androidx.appcompat.widget.AppCompatImageView
import coil.Coil
import coil.annotation.ExperimentalCoilApi
import coil.api.load
import coil.request.LoadRequest
import coil.request.RequestDisposable
import coil.transition.CrossfadeTransition
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.image.contract.ITmdbImage

fun AppCompatImageView.setImageUrl(url: String?) = url?.let {
    load(url) {
        crossfade(350)
    }
}

@OptIn(ExperimentalCoilApi::class)
fun AppCompatImageView.using(entity: ITmdbImage): RequestDisposable? {
    val requestBuilder = LoadRequest.Builder(context)

    val request = requestBuilder
        .transition(CrossfadeTransition(350))
        .data(entity).target(this).build()

    return Coil.imageLoader(context).execute(request)
}
