package com.pinup.placePinup.di

import com.pinup.placePinup.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    single<AcceptPinBuddyUseCase> { AcceptPinBuddyUseCase(get()) }
    single<AddBookmarkUseCase> { AddBookmarkUseCase(get()) }
    single<ChangePasswordUseCase> { ChangePasswordUseCase(get()) }
    single<CheckNickNameUseCase> { CheckNickNameUseCase(get()) }
    single<DeleteBookmarkUseCase> { DeleteBookmarkUseCase(get()) }
    single<DeleteCommentUseCase> { DeleteCommentUseCase(get()) }
    single<DeletePinlogUseCase> { DeletePinlogUseCase(get()) }
    single<DeletePinBuddyUseCase> { DeletePinBuddyUseCase(get()) }
    single<DeletePintsUseCase> { DeletePintsUseCase(get()) }
    single<DeleteRecentSearchUseCase> { DeleteRecentSearchUseCase(get()) }
    single<DeleteRecentPinBuddySearchUseCase> { DeleteRecentPinBuddySearchUseCase(get()) }
    single<DeleteRequestPinBuddyUseCase> { DeleteRequestPinBuddyUseCase(get()) }
    single<EditCommentUseCase> { EditCommentUseCase(get()) }
    single<EditPinlogUseCase> { EditPinlogUseCase(get()) }
    single<EditProfileUseCase> { EditProfileUseCase(get()) }
    single<EditPintsUseCase> { EditPintsUseCase(get()) }
    single<GetBookmarksUseCase> { GetBookmarksUseCase(get()) }
    single<GetDetailPlaceUseCase> { GetDetailPlaceUseCase(get()) }
    single<GetEditorPintsUseCase> { GetEditorPintsUseCase(get()) }
    single<GetEditorPintsCategoryUseCase> { GetEditorPintsCategoryUseCase(get()) }
    single<GetEditorPintsDetailUseCase> { GetEditorPintsDetailUseCase(get()) }
    single<GetFeedUseCase> { GetFeedUseCase(get()) }
    single<GetFindIdByEmailUseCase> { GetFindIdByEmailUseCase(get()) }
    single<GetFindIdByNicknameUseCase> { GetFindIdByNicknameUseCase(get()) }
    single<GetMemberInfoUseCase> { GetMemberInfoUseCase(get()) }
    single<GetMyProfileUseCase> { GetMyProfileUseCase(get()) }
    single<GetPhotoReviewsUseCase> { GetPhotoReviewsUseCase(get()) }
    single<GetPinBuddiesUseCase> { GetPinBuddiesUseCase(get()) }
    single<GetPinlogDetailUseCase> { GetPinlogDetailUseCase(get()) }
    single<GetPintsUseCase> { GetPintsUseCase(get()) }
    single<GetPintsDetailUseCase> { GetPintsDetailUseCase(get()) }
    single<GetRecentSearchUseCase> { GetRecentSearchUseCase(get()) }
    single<GetRecentPinBuddySearchUseCase> { GetRecentPinBuddySearchUseCase(get()) }
    single<GetReceivePinBuddyRequestsUseCase> { GetReceivePinBuddyRequestsUseCase(get()) }
    single<GetReviewedPlacesUseCase> { GetReviewedPlacesUseCase(get()) }
    single<GetSentPinBuddyRequestsUseCase> { GetSentPinBuddyRequestsUseCase(get()) }
    single<GetTextReviewsUseCase> { GetTextReviewsUseCase(get()) }
    single<IsLoginUseCase> { IsLoginUseCase(get(), get(), get()) }
    single<PostCommentUseCase> { PostCommentUseCase(get()) }
    single<PostEmailVerifyUseCase> { PostEmailVerifyUseCase(get()) }
    single<PostImageUploadUseCase> { PostImageUploadUseCase(get()) }
    single<PostReviewLikeChangeUseCase> { PostReviewLikeChangeUseCase(get()) }
    single<PostSendVerifyCodeUseCase> { PostSendVerifyCodeUseCase(get()) }
    single<PostTemporaryPasswordUseCase> { PostTemporaryPasswordUseCase(get()) }
    single<PostSetDeviceTokenUseCase> { PostSetDeviceTokenUseCase(get()) }
    single<PostSeveralImagesUploadUseCase> { PostSeveralImagesUploadUseCase(get()) }
    single<SocialLoginUseCase> { SocialLoginUseCase(get(), get(), get()) }
    single<EmailLoginUseCase> { EmailLoginUseCase(get(), get(), get()) }
    single<LogoutUseCase> { LogoutUseCase(get(), get()) }
    single<RegisterReviewUseCase> { RegisterReviewUseCase(get()) }
    single<RegisterPintsUseCase> { RegisterPintsUseCase(get()) }
    single<RejectPinBuddyUseCase> { RejectPinBuddyUseCase(get()) }
    single<RequestPinBuddyUseCase> { RequestPinBuddyUseCase(get()) }
    single<SaveRecentSearchUseCase> { SaveRecentSearchUseCase(get()) }
    single<SaveRecentPinBuddySearchUseCase> { SaveRecentPinBuddySearchUseCase(get()) }
    single<SaveTokenUseCase> { SaveTokenUseCase(get()) }
    single<SaveUserInfoUseCase> { SaveUserInfoUseCase(get()) }
    single<SearchPlacesUseCase> { SearchPlacesUseCase(get()) }
    single<SearchUserUseCase> { SearchUserUseCase(get()) }
    single<SocialSignUpUseCase> { SocialSignUpUseCase(get(), get(), get()) }
    single<EmailSignUpUseCase> { EmailSignUpUseCase(get(), get(), get()) }
    single<UnRegisterUseCase> { UnRegisterUseCase(get()) }
    single<PostUserBlockUseCase> { PostUserBlockUseCase(get()) }
    single<PostUserUnBlockUseCase> { PostUserUnBlockUseCase(get()) }
    single<PostReportUserUseCase> { PostReportUserUseCase(get()) }
    single<PostReportPinlogUseCase> { PostReportPinlogUseCase(get()) }
    single<PostReportCommentUseCase> { PostReportCommentUseCase(get()) }
}