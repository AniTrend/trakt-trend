package io.wax911.trakt.domain.entities.shared

import io.wax911.trakt.domain.entities.shared.contract.ISharedMediaWithImage
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.show.contract.ISharedMedia

data class ShowWithImages(
    override val media: ISharedMedia,
    override val images: Collection<IShowImage>,
    override val id: Int = 0
) : ISharedMediaWithImage