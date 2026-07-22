package com.currency.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) providing methods for querying the local Room database.
 */
@Dao
interface MarketDao {

    /**
     * Retrieves all cached market items sorted by asset type.
     * Returns a reactive Flow that emits whenever the table updates.
     */
    @Query("SELECT * FROM market_items ORDER BY assetType DESC")
    fun getAllMarketItems(): Flow<List<MarketEntity>>

    /**
     * Inserts or updates fetched remote market items into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarketItems(items: List<MarketEntity>)

    /**
     * Clears all cached items in the table (useful during full refreshes).
     */
    @Query("DELETE FROM market_items")
    suspend fun clearAll()
}