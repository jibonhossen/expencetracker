package com.horizonlab.expencetracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.horizonlab.expencetracker.data.dao.AppDao
import com.horizonlab.expencetracker.data.entity.ExpenseEntity
import com.horizonlab.expencetracker.data.entity.GroceryEntity
import com.horizonlab.expencetracker.data.entity.SettingsEntity

@Database(
    entities = [SettingsEntity::class, GroceryEntity::class, ExpenseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
