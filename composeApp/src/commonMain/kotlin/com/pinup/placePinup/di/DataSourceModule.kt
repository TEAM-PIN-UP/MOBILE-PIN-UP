package com.pinup.placePinup.di

import com.pinup.placePinup.data.local.*
import com.pinup.placePinup.data.remote.*
import com.pinup.placePinup.local.impl.*
import com.pinup.placePinup.remote.impl.*

import org.koin.dsl.module

val dataSourceModule = module {
    single<EmailVerifyDataSource> { EmailVerifyDataSourceImpl(get()) }
    single<AuthRemoteDataSource> { AuthRemoteDataSourceImpl(get(), get()) }
    single<ImageRemoteDataSource> { ImageRemoteDataSourceImpl(get()) }
    single<BookmarksRemoteDataSource> { BookmarksRemoteDataSourceImpl(get()) }
    single<MembersRemoteDataSource> { MembersRemoteDataSourceImpl(get()) }
    single<MembersLocalDataSource> { MembersLocalDataSourceImpl(get()) }
    single<ReviewsLocalDataSource> { ReviewsLocalDataSourceImpl(get()) }
    single<PinBuddyRemoteDataSource> { PinBuddyRemoteDataSourceImpl(get()) }
    single<PlacesRemoteDataSource> { PlacesRemoteDataSourceImpl(get()) }
    single<ReviewsRemoteDataSource> { ReviewsRemoteDataSourceImpl(get()) }
    single<PintsRemoteDataSource> { PintsRemoteDataSourceImpl(get()) }
    single<ReportRemoteDataSource> { ReportRemoteDataSourceImpl(get()) }
}