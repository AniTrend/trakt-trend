package io.wax911.trakt.domain.entities.shared.contract

import io.wax911.trakt.domain.entities.contract.IEntity
import io.wax911.trakt.domain.entities.image.contract.IShowImage
import io.wax911.trakt.domain.entities.show.contract.ISharedMedia

interface ISharedMediaWithImage : IEntity {
    val media: ISharedMedia
    val images: Collection<IShowImage>
}