package com.pinup.pinup.di

import com.pinup.pinup.data.local.*
import com.pinup.pinup.data.remote.*
import com.pinup.pinup.local.impl.*
import com.pinup.pinup.remote.impl.*

import org.koin.dsl.module

val dataSourceModule = module {
    single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get()) }
    single<BookmarksRemoteDataSource> { BookmarksRemoteDataSourceImpl(get()) }
    single<MembersRemoteDataSource> { MembersRemoteDataSourceImpl(get()) }
    single<MembersLocalDataSource> { MembersLocalDataSourceImpl(get()) }
    single<PinBuddyRemoteDataSource> { PinBuddyRemoteDataSourceImpl(get()) }
    single<PlacesRemoteDataSource> { PlacesRemoteDataSourceImpl(get()) }
    single<ReviewsRemoteDataSource> { ReviewsRemoteDataSourceImpl(get()) }
}