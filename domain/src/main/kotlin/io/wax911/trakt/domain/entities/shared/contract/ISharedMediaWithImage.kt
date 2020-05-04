package io.wax911.trakt.domain.entities.shared.contract

import io.wax911.trakt.domain.entities.image.contract.ITmdbImage
import io.wax911.trakt.domain.entities.show.contract.ISharedMedia

interface ISharedMediaWithImage {
    val media: ISharedMedia
    val image: ITmdbImage
}