package com.horizonlab.expencetracker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.horizonlab.expencetracker.data.dao.AppDao
import com.horizonlab.expencetracker.data.dao.CategoryTotal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

data class DashboardUiState(
    val userName: String = "User",
    val currencySymbol: String = "$",
    val monthlyBudget: Int = 0,
    val totalSpent: Int = 0,
    val remaining: Int = 0,
    val dailyAverage: Int = 0,
    val percentageUsed: Float = 0f,
    val categoryBreakdown: List<CategoryTotal> = emptyList()
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dao: AppDao
) : ViewModel() {

    private val calendar = Calendar.getInstance()

    private val monthStart: Long
        get() {
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, 1)
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            return cal.timeInMillis
        }

    private val monthEnd: Long
        get() {
            val cal = Calendar.getInstance()
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
            cal.set(Calendar.HOUR_OF_DAY, 23)
            cal.set(Calendar.MINUTE, 59)
            cal.set(Calendar.SECOND, 59)
            cal.set(Calendar.MILLISECOND, 999)
            return cal.timeInMillis
        }

    private val currentDayOfMonth: Int
        get() = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    val uiState: StateFlow<DashboardUiState> = combine(
        dao.getSettings(),
        dao.getTotalExpenses(monthStart, monthEnd),
        dao.getExpensesByCategory(monthStart, monthEnd)
    ) { settings, totalSpent, categories ->
        val budget = settings?.monthlyBudget ?: 0
        val spent = totalSpent
        val remaining = budget - spent
        val dailyAvg = if (currentDayOfMonth > 0) spent / currentDayOfMonth else 0
        val percentage = if (budget > 0) spent.toFloat() / budget.toFloat() else 0f

        DashboardUiState(
            userName = settings?.name ?: "User",
            currencySymbol = settings?.currencySymbol ?: "$",
            monthlyBudget = budget,
            totalSpent = spent,
            remaining = remaining,
            dailyAverage = dailyAvg,
            percentageUsed = percentage,
            categoryBreakdown = categories
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )
}
