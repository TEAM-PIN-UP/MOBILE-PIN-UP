package com.pinup.pinup.remote.di

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pinup.pinup.BuildConfig
import com.pinup.pinup.data.local.MembersLocalDataSource
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.TokenInfo
import com.pinup.pinup.domain.model.mapSuccessData
import com.pinup.pinup.event.LogoutEventBus
import com.pinup.pinup.remote.api.AuthApi
import com.pinup.pinup.remote.network.NetworkCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        addHeaderInterceptor: AddHeaderInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): OkHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().let {
                it.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(it)
            }
        }
        addInterceptor(addHeaderInterceptor)
        authenticator(tokenAuthenticator)
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(NetworkCallAdapterFactory())
            .build()
    }
}
class AddHeaderInterceptor (
    private val membersLocalDataSource: MembersLocalDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
        val accessToken = runBlocking {
            membersLocalDataSource.getAccessToken()
        }
        hLog("accessToken >>> $accessToken")
        val newRequest: Request = if (accessToken.isNotEmpty()) {
            request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        } else {
            request().newBuilder()
                .build()
        }
        hLog("newRequest >>> ${newRequest.headers}")
        proceed(newRequest)
    }
}

class TokenAuthenticator (
    private val membersLocalDataSource: MembersLocalDataSource
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val usedAccessToken = runBlocking { membersLocalDataSource.getAccessToken() }
        var newRequest: Request? = null
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED && usedAccessToken.isNotBlank()) {
            synchronized(this) {
                val currentToken = runBlocking { membersLocalDataSource.getAccessToken() }
                if (currentToken == usedAccessToken) {
                    // 이전 요청에서 넣었던 토큰과 현재 토큰이 같으면 토큰을 갱신
                    refreshToken(
                        onSuccess = {
                            val newToken = runBlocking { membersLocalDataSource.getAccessToken() }
                            newRequest = response.request.withNewAccessToken(newToken)
                        },
                        onRefreshTokenExpired = {
                            newRequest = response.request.withRemoveAccessToken()
                            logout(it)
                        }
                    )
                } else if (currentToken.isBlank()) {
                    newRequest = response.request.withRemoveAccessToken()
                } else {
                    newRequest = response.request.withNewAccessToken(currentToken)
                }
            }
        }
        return newRequest
    }

    private fun logout(message: String) {
        runBlocking {
            LogoutEventBus.sendEvent(message)
        }
    }

    private fun refreshToken(onSuccess: () -> Unit, onRefreshTokenExpired: (String) -> Unit) {
        val authApi = getRetrofit().create(AuthApi::class.java)
        val refreshTokenResponse = runBlocking(Dispatchers.IO) {
            authApi.refreshToken(
                token = membersLocalDataSource.getRefreshToken()
            ).mapSuccessData()
        }

        when (refreshTokenResponse) {
            is PResult.Fail -> {
                when (refreshTokenResponse.failState.status) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                        onRefreshTokenExpired(refreshTokenResponse.failState.message)
                    }
                }
            }
            is PResult.Success -> {
                runBlocking(Dispatchers.IO) {
                    membersLocalDataSource.saveToken(TokenInfo(
                        accessToken = refreshTokenResponse.data.accessToken,
                        refreshToken = refreshTokenResponse.data.refreshToken
                    ))
                    onSuccess()
                }
            }
        }
    }

    private fun getOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().let {
                it.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(it)
            }
        }
    }.build()

    private fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(getOkHttpClient())
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(NetworkCallAdapterFactory())
            .build()

    private fun Request.withNewAccessToken(accessToken: String) =
        newBuilder()
            .removeHeader("Authorization")
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

    private fun Request.withRemoveAccessToken() =
        newBuilder()
            .removeHeader("Authorization")
            .build()
}
