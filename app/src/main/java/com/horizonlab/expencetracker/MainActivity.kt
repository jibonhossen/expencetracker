package com.horizonlab.expencetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.horizonlab.expencetracker.ui.dashboard.DashboardScreen
import com.horizonlab.expencetracker.ui.expenses.ExpensesScreen
import com.horizonlab.expencetracker.ui.grocery.GroceryScreen
import com.horizonlab.expencetracker.ui.settings.SettingsScreen
import com.horizonlab.expencetracker.ui.theme.ExpenceTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenceTrackerTheme {
                ExpenseTrackerAppScreen()
            }
        }
    }
}

// ── Navigation Routes ────────────────────────────────────────────

@Serializable object DashboardRoute
@Serializable object GroceryRoute
@Serializable object ExpensesRoute
@Serializable object SettingsRoute

data class TopLevelRoute<T : Any>(
    val label: String,
    val route: T,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val topLevelRoutes = listOf(
    TopLevelRoute("Dashboard", DashboardRoute, Icons.Filled.Dashboard, Icons.Outlined.Dashboard),
    TopLevelRoute("Grocery", GroceryRoute, Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
    TopLevelRoute("Expenses", ExpensesRoute, Icons.Filled.Receipt, Icons.Outlined.Receipt),
    TopLevelRoute("Settings", SettingsRoute, Icons.Filled.Settings, Icons.Outlined.Settings),
)

// ── App Shell ────────────────────────────────────────────────────

@Composable
fun ExpenseTrackerAppScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                topLevelRoutes.forEach { topLevelRoute ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.hasRoute(topLevelRoute.route::class)
                    } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(topLevelRoute.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                if (selected) topLevelRoute.selectedIcon else topLevelRoute.unselectedIcon,
                                contentDescription = topLevelRoute.label
                            )
                        },
                        label = { Text(topLevelRoute.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DashboardRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<DashboardRoute> { DashboardScreen() }
            composable<GroceryRoute> { GroceryScreen() }
            composable<ExpensesRoute> { ExpensesScreen() }
            composable<SettingsRoute> { SettingsScreen() }
        }
    }
}