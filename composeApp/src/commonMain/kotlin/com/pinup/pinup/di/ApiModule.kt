package com.pinup.pinup.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.pinup.pinup.remote.api.*

val apiModule = module {
    singleOf(::AuthApi)
    singleOf(::BookmarksApi)
    singleOf(::MembersApi)
    singleOf(::PinBuddyApi)
    singleOf(::PlacesApi)
    singleOf(::ReviewsApi)
}