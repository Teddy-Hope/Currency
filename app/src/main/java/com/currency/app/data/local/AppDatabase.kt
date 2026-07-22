package com.currency.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The main Room Database configuration for caching financial data offline.
 */
@Database(entities = [MarketEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    
    /**
     * Connects the database to its data access interfaces.
     */
    abstract fun marketDao(): MarketDao
}