package com.horizonlab.expencetracker.ui.grocery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horizonlab.expencetracker.data.dao.AppDao
import com.horizonlab.expencetracker.data.entity.ExpenseEntity
import com.horizonlab.expencetracker.data.entity.GroceryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceryViewModel @Inject constructor(
    private val dao: AppDao
) : ViewModel() {

    val groceries: StateFlow<List<GroceryEntity>> = dao.getAllGroceries()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addGrocery(name: String, tag: String) {
        viewModelScope.launch {
            dao.insertGrocery(
                GroceryEntity(
                    name = name,
                    tag = tag,
                    dateAdded = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteGrocery(grocery: GroceryEntity) {
        viewModelScope.launch {
            // If the grocery was completed, also remove its auto-generated expense
            if (grocery.isCompleted) {
                dao.deleteExpenseByGrocery(grocery.name, grocery.dateAdded)
            }
            dao.deleteGrocery(grocery)
        }
    }

    fun completeGrocery(grocery: GroceryEntity, price: Int) {
        viewModelScope.launch {
            // Mark grocery as completed with price
            dao.updateGrocery(
                grocery.copy(isCompleted = true, price = price)
            )
            // Auto-create expense entry
            dao.insertExpense(
                ExpenseEntity(
                    amount = price,
                    category = "Groceries",
                    date = grocery.dateAdded,
                    description = grocery.name
                )
            )
        }
    }

    fun uncompleteGrocery(grocery: GroceryEntity) {
        viewModelScope.launch {
            // Remove the auto-generated expense
            dao.deleteExpenseByGrocery(grocery.name, grocery.dateAdded)
            // Revert grocery to incomplete
            dao.updateGrocery(
                grocery.copy(isCompleted = false, price = null)
            )
        }
    }
}
