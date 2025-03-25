package com.pinup.pinup.di

import com.pinup.pinup.data.impl.AuthRepositoryImpl
import com.pinup.pinup.data.impl.BookmarksRepositoryImpl
import com.pinup.pinup.data.impl.MembersRepositoryImpl
import com.pinup.pinup.data.impl.PinBuddyRepositoryImpl
import com.pinup.pinup.data.impl.PlacesRepositoryImpl
import com.pinup.pinup.data.impl.ReviewsRepositoryImpl
import com.pinup.pinup.domain.repository.AuthRepository
import com.pinup.pinup.domain.repository.BookmarksRepository
import com.pinup.pinup.domain.repository.MembersRepository
import com.pinup.pinup.domain.repository.PinBuddyRepository
import com.pinup.pinup.domain.repository.PlacesRepository
import com.pinup.pinup.domain.repository.ReviewsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<BookmarksRepository> { BookmarksRepositoryImpl(get()) }
    single<MembersRepository> { MembersRepositoryImpl(get(), get()) }
    single<PinBuddyRepository> { PinBuddyRepositoryImpl(get()) }
    single<PlacesRepository> { PlacesRepositoryImpl(get()) }
    single<ReviewsRepository> { ReviewsRepositoryImpl(get()) }
}