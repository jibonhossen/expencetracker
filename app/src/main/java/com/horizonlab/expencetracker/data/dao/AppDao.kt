package com.horizonlab.expencetracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.horizonlab.expencetracker.data.entity.ExpenseEntity
import com.horizonlab.expencetracker.data.entity.GroceryEntity
import com.horizonlab.expencetracker.data.entity.SettingsEntity
import kotlinx.coroutines.flow.Flow

data class CategoryTotal(
    val category: String,
    val total: Int
)

@Dao
interface AppDao {

    // ── Settings ──────────────────────────────────────────────────────

    @Query("SELECT * FROM settings LIMIT 1")
    fun getSettings(): Flow<SettingsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: SettingsEntity)

    @Update
    suspend fun updateSettings(settings: SettingsEntity)

    // ── Groceries ─────────────────────────────────────────────────────

    @Query("SELECT * FROM groceries ORDER BY isCompleted ASC, dateAdded DESC")
    fun getAllGroceries(): Flow<List<GroceryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrocery(grocery: GroceryEntity)

    @Update
    suspend fun updateGrocery(grocery: GroceryEntity)

    @Delete
    suspend fun deleteGrocery(grocery: GroceryEntity)

    // ── Expenses ──────────────────────────────────────────────────────

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startMs AND :endMs ORDER BY date DESC")
    fun getExpensesByMonth(startMs: Long, endMs: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT category, SUM(amount) as total FROM expenses WHERE date BETWEEN :startMs AND :endMs GROUP BY category ORDER BY total DESC")
    fun getExpensesByCategory(startMs: Long, endMs: Long): Flow<List<CategoryTotal>>

    @Query("SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE date BETWEEN :startMs AND :endMs")
    fun getTotalExpenses(startMs: Long, endMs: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE category = 'Groceries' AND description = :groceryName AND date = :date")
    suspend fun deleteExpenseByGrocery(groceryName: String, date: Long)
}
