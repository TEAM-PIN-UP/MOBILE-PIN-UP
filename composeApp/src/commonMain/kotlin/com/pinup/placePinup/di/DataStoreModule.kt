package com.pinup.placePinup.di

import com.pinup.placePinup.platform.dataStorePreferences
import org.koin.dsl.module

val dataStoreModule = module {
    single {
        dataStorePreferences()
    }
}