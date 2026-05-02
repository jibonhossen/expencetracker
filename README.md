# <p align="center"><img src="assets/logo.png" alt="EXPTRA Logo" width="120"></p>
# <p align="center">EXPTRA - Premium Expense & Grocery Tracker</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Kotlin-1.9.0-purple.svg" alt="Kotlin">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg" alt="Compose">
  <img src="https://img.shields.io/badge/Database-Room-orange.svg" alt="Room">
</p>

EXPTRA is a sleek, offline-first Android application designed to help you manage your finances and grocery shopping with elegance and ease. Built with modern Android technologies, it provides a seamless experience for tracking daily spending and planning your shopping.

---

## ✨ Features

- **📊 Dynamic Dashboard**: Get an instant overview of your monthly budget, remaining balance, and daily spending averages.
- **🛒 Smart Grocery List**: Organize your shopping with category tags. Mark items as complete and automatically track their cost as an expense.
- **💸 Detailed Expense Tracking**: A comprehensive history of all your transactions, including those automatically generated from your grocery runs.
- **🎨 Material 3 Design**: A premium, modern UI with dynamic color support and a sleek monochrome aesthetic.
- **🔒 Offline First**: Your data stays on your device. Powered by Room Database for fast, reliable local storage.
- **⚙️ Personalized Settings**: Customize your name, monthly budget, and currency symbol to suit your needs.

---

## 📱 Preview

<p align="center">
  <img src="assets/showcase.png" alt="EXPTRA Showcase" width="800">
</p>

---

## 🛠️ Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Database**: [Room](https://developer.android.com/training/data-storage/room) (SQLite)
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: Navigation Compose

---

## 🏗️ Architecture

EXPTRA follows the recommended Android architecture patterns for scalability and maintainability:

- **UI Layer**: Jetpack Compose screens that observe state from ViewModels.
- **Domain Layer**: ViewModels handle business logic and expose `StateFlow` to the UI.
- **Data Layer**: Room Database and DAOs provide a robust local data source.

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Iguana or newer
- JDK 17
- Android SDK 26+

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/jibonhossen/expencetracker.git
   ```
2. Open the project in Android Studio.
3. Sync Project with Gradle Files.
4. Run the `app` on your emulator or physical device.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<p align="center">Made with ❤️ for better financial management.</p>
