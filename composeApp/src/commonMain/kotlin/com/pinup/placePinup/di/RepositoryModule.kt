package com.pinup.placePinup.di

import com.pinup.placePinup.data.impl.AuthRepositoryImpl
import com.pinup.placePinup.data.impl.BookmarksRepositoryImpl
import com.pinup.placePinup.data.impl.EmailVerifyRepositoryImpl
import com.pinup.placePinup.data.impl.ImageRepositoryImpl
import com.pinup.placePinup.data.impl.MembersRepositoryImpl
import com.pinup.placePinup.data.impl.PinBuddyRepositoryImpl
import com.pinup.placePinup.data.impl.PintsRepositoryImpl
import com.pinup.placePinup.data.impl.PlacesRepositoryImpl
import com.pinup.placePinup.data.impl.ReportRepositoryImpl
import com.pinup.placePinup.data.impl.ReviewsRepositoryImpl
import com.pinup.placePinup.domain.repository.AuthRepository
import com.pinup.placePinup.domain.repository.BookmarksRepository
import com.pinup.placePinup.domain.repository.EmailVerifyRepository
import com.pinup.placePinup.domain.repository.ImageRepository
import com.pinup.placePinup.domain.repository.MembersRepository
import com.pinup.placePinup.domain.repository.PinBuddyRepository
import com.pinup.placePinup.domain.repository.PintsRepository
import com.pinup.placePinup.domain.repository.PlacesRepository
import com.pinup.placePinup.domain.repository.ReportRepository
import com.pinup.placePinup.domain.repository.ReviewsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<EmailVerifyRepository> { EmailVerifyRepositoryImpl(get()) }
    single<ImageRepository> { ImageRepositoryImpl(get()) }
    single<BookmarksRepository> { BookmarksRepositoryImpl(get()) }
    single<MembersRepository> { MembersRepositoryImpl(get(), get()) }
    single<PinBuddyRepository> { PinBuddyRepositoryImpl(get()) }
    single<PlacesRepository> { PlacesRepositoryImpl(get()) }
    single<ReviewsRepository> { ReviewsRepositoryImpl(get(), get()) }
    single<PintsRepository> { PintsRepositoryImpl(get()) }
    single<ReportRepository> { ReportRepositoryImpl(get()) }
}