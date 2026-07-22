package com.currency.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Base Application class triggered first upon startup.
 * Annotated with HiltAndroidApp to kickstart code generation for Dependency Injection.
 */
@HiltAndroidApp
class CurrencyApplication : Application()