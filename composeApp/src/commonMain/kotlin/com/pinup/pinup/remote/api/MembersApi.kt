package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.ProfileEditRequest
import com.pinup.pinup.data.request.signUp.EmailSignUpRequest
import com.pinup.pinup.data.request.signUp.SocialSignUpRequest
import com.pinup.pinup.data.response.CheckNickNameResponse
import com.pinup.pinup.data.response.FindIdResponse
import com.pinup.pinup.data.response.GetMemberInfoResponse
import com.pinup.pinup.data.response.GetReviewsResponse
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.data.response.SearchUserResponse
import com.pinup.pinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface MembersApi {
    @GET("api/members/nickname/check")
    suspend fun checkNickName(@Query("nickname") nickname: String): PResult<PResponse<CheckNickNameResponse>>

    @GET("api/members/search")
    suspend fun searchUser(@Query("nickname") nickname: String): PResult<PResponse<List<SearchUserResponse>>>

    @GET("api/members/{memberId}")
    suspend fun getMemberInfo(@Path("memberId") memberId: Int): PResult<PResponse<GetMemberInfoResponse>>

    @GET("api/members")
    suspend fun getMemberInfo(): PResult<PResponse<GetMemberInfoResponse>>

    @GET("api/members/{memberId}/text-reviews")
    suspend fun getTextReviews(
        @Path("memberId") memberId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): PResult<PResponse<GetReviewsResponse>>

    @GET("api/members/me/text-reviews")
    suspend fun getTextReviews(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): PResult<PResponse<GetReviewsResponse>>

    @GET("api/members/{memberId}/photo-reviews")
    suspend fun getPhotoReviews(
        @Path("memberId") memberId: Int,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): PResult<PResponse<GetReviewsResponse>>

    @GET("api/members/me/photo-reviews")
    suspend fun getPhotoReviews(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): PResult<PResponse<GetReviewsResponse>>

    @POST(ApiPath.Members.SOCIAL_SIGN_UP)
    suspend fun socialSignUp(
        @Body request: SocialSignUpRequest
    ): PResult<PResponse<LoginResponse>>

    @POST(ApiPath.Members.EMAIL_SIGN_UP)
    suspend fun emailSignUp(
        @Body request: EmailSignUpRequest
    ): PResult<PResponse<LoginResponse>>

    @PUT(ApiPath.Members.MEMBERS)
    suspend fun editProfile(
        @Body request: ProfileEditRequest
    ): PResult<PResponse<Unit>>

    @DELETE(ApiPath.Members.MEMBERS)
    suspend fun unregister(): PResult<PResponse<Unit>>

    @GET(ApiPath.Members.FIND_ID_BY_NICKNAME)
    suspend fun getFindIdByNickname(
        @Query("nickname") nickname: String
    ): PResult<PResponse<FindIdResponse>>

    @GET(ApiPath.Members.FIND_ID_BY_EMAIL)
    suspend fun getFindIdByEmail(
        @Query("email") email: String
    ): PResult<PResponse<FindIdResponse>>
}