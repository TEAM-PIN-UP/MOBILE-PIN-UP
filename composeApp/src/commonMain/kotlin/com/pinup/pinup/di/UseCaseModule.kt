package com.pinup.pinup.di

import com.pinup.pinup.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    single<AcceptPinBuddyUseCase> { AcceptPinBuddyUseCase(get()) }
    single<AddBookmarkUseCase> { AddBookmarkUseCase(get()) }
    single<CheckNickNameUseCase> { CheckNickNameUseCase(get()) }
    single<DeleteBookmarkUseCase> { DeleteBookmarkUseCase(get()) }
    single<DeleteCommentUseCase> { DeleteCommentUseCase(get()) }
    single<DeletePinlogUseCase> { DeletePinlogUseCase(get()) }
    single<DeletePinBuddyUseCase> { DeletePinBuddyUseCase(get()) }
    single<DeleteRequestPinBuddyUseCase> { DeleteRequestPinBuddyUseCase(get()) }
    single<EditCommentUseCase> { EditCommentUseCase(get()) }
    single<EditPinlogUseCase> { EditPinlogUseCase(get()) }
    single<GetBookmarksUseCase> { GetBookmarksUseCase(get()) }
    single<GetDetailPlaceUseCase> { GetDetailPlaceUseCase(get()) }
    single<GetFeedUseCase> { GetFeedUseCase(get()) }
    single<GetMemberInfoUseCase> { GetMemberInfoUseCase(get()) }
    single<GetMyProfileUseCase> { GetMyProfileUseCase(get()) }
    single<GetPhotoReviewsUseCase> { GetPhotoReviewsUseCase(get()) }
    single<GetPinBuddiesUseCase> { GetPinBuddiesUseCase(get()) }
    single<GetPinlogDetailUseCase> { GetPinlogDetailUseCase(get()) }
    single<GetReceivePinBuddyRequestsUseCase> { GetReceivePinBuddyRequestsUseCase(get()) }
    single<GetReviewedPlacesUseCase> { GetReviewedPlacesUseCase(get()) }
    single<GetSentPinBuddyRequestsUseCase> { GetSentPinBuddyRequestsUseCase(get()) }
    single<GetTextReviewsUseCase> { GetTextReviewsUseCase(get()) }
    single<IsLoginUseCase> { IsLoginUseCase(get(), get(), get()) }
    single<PostCommentUseCase> { PostCommentUseCase(get()) }
    single<PostEmailVerifyUseCase> { PostEmailVerifyUseCase(get()) }
    single<PostImageUploadUseCase> { PostImageUploadUseCase(get()) }
    single<PostSendVerifyCodeUseCase> { PostSendVerifyCodeUseCase(get()) }
    single<PostSeveralImagesUploadUseCase> { PostSeveralImagesUploadUseCase(get()) }
    single<SocialLoginUseCase> { SocialLoginUseCase(get(), get(), get()) }
    single<EmailLoginUseCase> { EmailLoginUseCase(get(), get(), get()) }
    single<LogoutUseCase> { LogoutUseCase(get(), get()) }
    single<RegisterReviewUseCase> { RegisterReviewUseCase(get()) }
    single<RejectPinBuddyUseCase> { RejectPinBuddyUseCase(get()) }
    single<RequestPinBuddyUseCase> { RequestPinBuddyUseCase(get()) }
    single<SaveTokenUseCase> { SaveTokenUseCase(get()) }
    single<SaveUserInfoUseCase> { SaveUserInfoUseCase(get()) }
    single<SearchPlacesUseCase> { SearchPlacesUseCase(get()) }
    single<SearchUserUseCase> { SearchUserUseCase(get()) }
    single<SocialSignUpUseCase> { SocialSignUpUseCase(get(), get(), get()) }
    single<EmailSignUpUseCase> { EmailSignUpUseCase(get(), get(), get()) }
}