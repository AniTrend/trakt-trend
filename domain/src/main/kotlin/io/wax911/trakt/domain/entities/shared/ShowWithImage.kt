package io.wax911.trakt.domain.entities.shared

import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.image.contract.ITmdbImage
import io.wax911.trakt.domain.entities.show.contract.ISharedMedia

data class ShowWithImage(
    override val media: ISharedMedia,
    override val image: ITmdbImage
) : ISharedMediaWithImage