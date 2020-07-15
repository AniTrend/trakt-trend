package io.wax911.trakt.data.arch.database.common

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete

@Dao
internal interface DaoQuery<T> {

    /**
     * Inserts a new item into the database ignoring items with the same primary key,
     * for both insert or update behavior use: [upsert]
     *
     * @param attribute item/s to insert
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(attribute: T)

    /**
     * Inserts new items into the database ignoring items with the same primary key,
     * for both insert or update behavior use: [upsert]
     *
     * @param attribute item/s to insert
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(attribute: List<T>)

    /**
     * Updates an item in the underlying database
     *
     * @param attribute item/s to update
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(attribute: T)

    /**
     * Updates a list of items in the underlying database
     *
     * @param attribute item/s to update
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(attribute: List<T>)

    /**
     * Deletes an item from the underlying database
     *
     * @param attribute item/s to delete
     */
    @Delete
    suspend fun delete(attribute: T)

    /**
     * Deletes a list of items from the underlying database
     *
     * @param attribute item/s to delete
     */
    @Delete
    suspend fun delete(attribute: List<T>)

    /**
     * Inserts or updates matching attributes on conflict
     *
     * @param attribute item/s to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(attribute: T)

    /**
     * Inserts or updates matching attributes on conflict
     *
     * @param attribute item/s to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(attribute: List<T>)
}