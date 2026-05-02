# Expense Tracker App Design Document

## 1. Overview
This document outlines the architecture, database schema, screens, and business logic of the Expense Tracker App. The app is a native Android application built using Kotlin, Jetpack Compose, Room Database, and Hilt for Dependency Injection. The primary goal is to manage groceries and track monthly expenses effectively.

## 2. Technology Stack
*   **Platform:** Native Android (Minimum SDK 26, Target SDK 34)
*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose (Material 3)
*   **Navigation:** Navigation Compose
*   **Local Database:** Room Database
*   **Dependency Injection:** Hilt
*   **Architecture Pattern:** MVVM (Model-View-ViewModel)

## 3. Database Schema (Room)
The app utilizes a local SQLite database managed by Room with the following three main entities.

### 3.1 SettingsEntity (`settings`)
Stores user preferences and budget configuration.
*   `id`: Int (Primary Key, Auto-generated)
*   `name`: String (Default: "User")
*   `monthlyBudget`: Int (Default: 0)
*   `currencySymbol`: String (Default: "$")

### 3.2 GroceryEntity (`groceries`)
Stores grocery items added by the user.
*   `id`: Int (Primary Key, Auto-generated)
*   `name`: String
*   `tag`: String (e.g., Produce, Dairy, Meat, Pantry, Frozen, Other)
*   `dateAdded`: Long (Timestamp)
*   `isCompleted`: Boolean (Default: false)
*   `price`: Int? (Nullable, assigned when marked as completed)

### 3.3 ExpenseEntity (`expenses`)
Stores tracked expenses, some of which are automatically generated from completed groceries.
*   `id`: Int (Primary Key, Auto-generated)
*   `amount`: Int
*   `category`: String
*   `date`: Long (Timestamp)
*   `description`: String? (Nullable)

## 4. UI Architecture & Navigation
The app uses a single-activity architecture (`MainActivity.kt`) hosting a root `ExpenseTrackerAppScreen`. 
Navigation is handled via `NavHost` combined with a bottom `NavigationBar`.

### Core Navigation Routes
*   `dashboard`: The main overview screen.
*   `grocery`: The grocery list management screen.
*   `expenses`: The detailed expense tracking screen.
*   `settings`: The app configuration screen.

## 5. Screen Breakdown & Features

### 5.1 Dashboard Screen (`DashboardScreen.kt`)
*   **Welcome Header:** Displays the user's name retrieved from settings.
*   **Premium Budget Card:**
    *   Shows "Monthly Budget" and the current percentage used based on total expenses.
    *   Color-coded progress indicator (Success: <50%, Warning: <80%, Error: >80%).
    *   Displays current spent amount vs total budget.
*   **Quick Stats:**
    *   **Remaining:** The leftover budget for the month.
    *   **Daily Avg:** Average spent per day in the current month.
*   **Category Breakdown:** Lists expenses aggregated by their respective categories.

### 5.2 Grocery Screen (`GroceryScreen.kt`)
*   **Add Item:** Floating Action Button opens an alert dialog to input the item name and select a category tag (Produce, Dairy, etc.) via filter chips.
*   **List View:** Displays grocery items using a `LazyColumn`. Items can be swiped to delete.
*   **Completion Flow:**
    *   Tapping the checkbox on an incomplete item triggers a "Set Price" dialog.
    *   Entering the price marks the item as `isCompleted = true` and updates the price.
    *   *Crucial Business Logic:* Completing an item automatically inserts a corresponding `ExpenseEntity` with the category "Groceries".
    *   Tapping a completed item unchecks it, reverting `isCompleted = false` and clearing the price.

### 5.3 Expenses Screen (`ExpensesScreen.kt`)
*   Displays a list of all tracked expenses (manually added or auto-generated from groceries).

### 5.4 Settings Screen (`SettingsScreen.kt`)
*   Allows the user to update `SettingsEntity`.
*   Includes inputs for Name, Monthly Budget, and Currency Symbol.

## 6. Business Logic & State Management
The MVVM pattern is implemented using ViewModels that expose data to the UI as `StateFlow`.
*   **DAOs (`AppDao.kt`):** Contains specific SQL queries for fetching, inserting, updating, and deleting records. Observes data using `Flow`.
*   **ViewModels:**
    *   `DashboardViewModel`: Calculates total expenses, budget progress, daily averages, and groups expenses by category.
    *   `GroceryViewModel`: Handles adding groceries, deleting, marking as complete/incomplete. Responsible for the cross-table logic of creating an expense when a grocery item is completed.
    *   `SettingsViewModel`: Manages auto-saving or updating the user preferences.
    *   `ExpensesViewModel`: Manages the expense list fetching and operations.

## 7. Theming & Styling
The app employs a custom Material 3 theme (`Theme.kt` & `Color.kt`).
*   Dynamic Color support for Android 12+.
*   A specific monochrome & sleek color palette fallback:
    *   Primary: Ink/Dark grey tones.
    *   Background: White for light mode, deep black (`#101010`) for dark mode.
    *   Surface colors have subtle elevated states.
*   Use of edge-to-edge transparent system bars.
