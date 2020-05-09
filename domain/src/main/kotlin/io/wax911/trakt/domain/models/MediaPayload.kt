package io.wax911.trakt.domain.models

import android.os.Parcelable
import io.wax911.trakt.domain.usecases.MediaRequestType
import kotlinx.android.parcel.Parcelize

@Parcelize
class MediaPayload(
    val request: MediaRequestType,
    val timePeriod: TimePeriod = TimePeriod.WEEKLY,
    val startDate: String = ""
) : Parcelable