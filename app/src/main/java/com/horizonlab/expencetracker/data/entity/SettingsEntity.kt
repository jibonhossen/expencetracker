package com.horizonlab.expencetracker.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "User",
    val monthlyBudget: Int = 0,
    val currencySymbol: String = "$"
)
