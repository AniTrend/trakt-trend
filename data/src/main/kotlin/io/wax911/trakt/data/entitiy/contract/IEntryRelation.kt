package io.wax911.trakt.data.entitiy.contract

interface IEntryRelation<E, R> {
    val entry: E?
    val reference: List<R>
}