package com.pinup.pinup.di

import com.pinup.pinup.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    single<AcceptPinBuddyUseCase> { AcceptPinBuddyUseCase(get()) }
    single<AddBookmarkUseCase> { AddBookmarkUseCase(get()) }
    single<CheckNickNameUseCase> { CheckNickNameUseCase(get()) }
    single<DeleteBookmarkUseCase> { DeleteBookmarkUseCase(get()) }
    single<DeletePinBuddyUseCase> { DeletePinBuddyUseCase(get()) }
    single<DeleteRequestPinBuddyUseCase> { DeleteRequestPinBuddyUseCase(get()) }
    single<GetBookmarksUseCase> { GetBookmarksUseCase(get()) }
    single<GetDetailPlaceUseCase> { GetDetailPlaceUseCase(get()) }
    single<GetMemberInfoUseCase> { GetMemberInfoUseCase(get()) }
    single<GetMyProfileUseCase> { GetMyProfileUseCase(get()) }
    single<GetPhotoReviewsUseCase> { GetPhotoReviewsUseCase(get()) }
    single<GetPinBuddiesUseCase> { GetPinBuddiesUseCase(get()) }
    single<GetReceivePinBuddyRequestsUseCase> { GetReceivePinBuddyRequestsUseCase(get()) }
    single<GetReviewedPlacesUseCase> { GetReviewedPlacesUseCase(get()) }
    single<GetSentPinBuddyRequestsUseCase> { GetSentPinBuddyRequestsUseCase(get()) }
    single<GetTextReviewsUseCase> { GetTextReviewsUseCase(get()) }
    single<IsLoginUseCase> { IsLoginUseCase(get(), get(), get()) }
    single<LoginUseCase> { LoginUseCase(get(), get(), get()) }
    single<LogoutUseCase> { LogoutUseCase(get(), get()) }
    single<RegisterReviewUseCase> { RegisterReviewUseCase(get()) }
    single<RejectPinBuddyUseCase> { RejectPinBuddyUseCase(get()) }
    single<RequestPinBuddyUseCase> { RequestPinBuddyUseCase(get()) }
    single<SaveTokenUseCase> { SaveTokenUseCase(get()) }
    single<SaveUserInfoUseCase> { SaveUserInfoUseCase(get()) }
    single<SearchPlacesUseCase> { SearchPlacesUseCase(get()) }
    single<SearchUserUseCase> { SearchUserUseCase(get()) }
    single<SignUpUseCase> { SignUpUseCase(get()) }
}