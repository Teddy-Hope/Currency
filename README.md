# Currency & Markets APP

A modern, production-ready Android application built with **Kotlin** and **Jetpack Compose** using **Clean Architecture** and **MVVM**. The app provides real-time financial tracking for the Ethiopian market and global financial ecosystems, featuring live currency exchange rates, global stock markets, cryptocurrency values, and an embedded Financial AI Assistant driven by Google Gemini.

---

## 🌟 Key Features

* **💱 Live Currency Exchange:** Track real-time foreign exchange rates across major Ethiopian commercial banks (CBE, Awash, Abyssinia, etc.) alongside official central bank rates.
* **📈 Global & Local Stock Markets:** Real-time updates on international stock indices (Apple, Tesla, S&P 500) and upcoming local market equities.
* **🪙 Cryptocurrency Tracker:** Dynamic tracking of top digital assets including Bitcoin (BTC), Ethereum (ETH), Tether (USDT), BNB, and USDC.
* **🤖 Financial AI Advisor:** An integrated chat assistant powered by **Google Gemini AI** capable of analyzing market trends, answering investment queries, and providing personalized financial context.
* **📊 Interactive Financial Charts:** Beautiful, highly responsive data visualizations and candlestick/line graphs using the **Vico Chart** library to observe historical market trends.
* **🖼️ Dynamic Asset Loading:** Seamless UI rendering with asynchronous remote asset loading powered by the **Coil Image Loader**.

---

## 🛠️ Architecture & Tech Stack

The architecture follows strict **Clean Architecture** principles separating the project into **Domain**, **Data**, and **Presentation** layers to maintain low coupling, high testability, and scalability.

* **Language:** Kotlin (100%)
* **UI Framework:** Jetpack Compose (Material 3)
* **Architecture Pattern:** MVVM (Model-View-ViewModel) + Clean Architecture
* **Asynchronous Flow:** Kotlin Coroutines & StateFlow
* **Dependency Injection:** Hilt (Dagger)
* **Networking:** Retrofit2 & OkHttp3 (with Logging Interceptors)
* **Local Database:** Room Database (for offline caching and persistence)
* **AI Engine:** Google Generative AI SDK (Gemini Pro)
* **CI/CD Pipeline:** Automated cloud compilation via GitHub Actions (Android CI wrapper integration for automated APK generation).

---

## 📁 Core Repository Structure

```text
app/src/main/java/com/currency/app/
│
├── data/                  # Data Layer: API services, Local DB room setups, and Repository Implementations
│   ├── local/
│   ├── network/
│   └── repository/
│
├── domain/                # Domain Layer: Business logic, Entities, and UseCases (Pure Kotlin)
│   ├── model/
│   ├── repository/
│   └── usecase/
│
└── presentation/          # Presentation Layer: UI components, Composables, and ViewModels
    ├── chat/              # Financial AI Chat Screen
    ├── dashboard/         # Markets and Exchange Dashboard
    └── theme/             # Design systems and Material 3 definitions
🚀 CI/CD & Automated Builds

This project leverages GitHub Actions workflows to run automated continuous integration checks. On every push to the main branch, the cloud compiler:

    Validates all Kotlin code signatures and handles dependencies structure matching.

    Runs the Android Gradle compiler environment under JDK 21.

    Automatically generates an installable .apk file available inside the Actions Artifacts tab.

📝 Future Roadmaps

    Implementation of real-time push notifications for rapid market price alerts.

    Multi-language localizations (including Amharic translation layouts).
    Deep analytics integration for advanced wallet budgeting advice.
