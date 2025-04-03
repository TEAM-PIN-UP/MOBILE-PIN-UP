package com.pinup.pinup.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(
    module: Module = module {},
    appDeclaration: KoinAppDeclaration = {},
) = startKoin {
    val modules = viewModelModule + repositoryModule + dataSourceModule + httpClientModule +
            apiModule + dataStoreModule + useCaseModule + module
        appDeclaration()
        modules(modules)
    }