package com.pinup.pinup.di

import com.pinup.pinup.dataStorePreferences
import org.koin.dsl.module

val dataStoreModule = module {
    single {
        dataStorePreferences()
    }
}