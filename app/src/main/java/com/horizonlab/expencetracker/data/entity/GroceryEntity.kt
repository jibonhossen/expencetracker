package com.horizonlab.expencetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groceries")
data class GroceryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val tag: String,
    val dateAdded: Long,
    val isCompleted: Boolean = false,
    val price: Int? = null
)
