package io.wax911.trakt.domain.entities.show

import io.wax911.trakt.domain.entities.show.contract.ISharedMedia

data class ShowItem(
    override val title: String,
    override val rating: Double,
    override val votes: Int,
    override val updated: Long,
    override val year: Int,
    override val id: Int
) : ISharedMedia