package com.pinup.placePinup.platform

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.pinup.placePinup.BuildConfig
import com.pinup.placePinup.ui.login.model.SNSType
import com.pinup.placePinup.ui.login.model.SNSUserInfo
import com.pinup.placePinup.ui.login.sns.SNSLoginController
import com.pinup.placePinup.ui.login.sns.SNSLoginResultListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections

actual class GoogleLoginController : SNSLoginController {
    @RequiresApi(Build.VERSION_CODES.O)
    actual override fun doLogin(resultListener: SNSLoginResultListener, context: Any) {
        val activityContext = context as Context
        val credentialManager = CredentialManager.create(activityContext)
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = credentialManager.getCredential(
                    context = activityContext,
                    request = buildRequest(filterByAuthorizedAccounts = true)
                )
                handleResult(result.credential, resultListener)
            } catch (e: NoCredentialException) {
                try {
                    val result = credentialManager.getCredential(
                        context = activityContext,
                        request = buildRequest(filterByAuthorizedAccounts = false)
                    )
                    handleResult(result.credential, resultListener)
                } catch (e2: NoCredentialException) {
                    hLog(e.message.orEmpty())
                    activityContext.startActivity(
                        Intent(Settings.ACTION_ADD_ACCOUNT).apply {
                            putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
                        }
                    )
                    resultListener.onFail(e2.message)
                }
            } catch (e: Exception) {
                resultListener.onFail(e.message)
            }
        }
    }

    private fun buildRequest(filterByAuthorizedAccounts: Boolean): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
            .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
            .setAutoSelectEnabled(false)
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun handleResult(
        credential: androidx.credentials.Credential,
        resultListener: SNSLoginResultListener
    ) {
        val googleCred = GoogleIdTokenCredential.createFrom(credential.data)

        val payload = withContext(Dispatchers.IO) {
            val verifier = GoogleIdTokenVerifier.Builder(
                NetHttpTransport(),
                GsonFactory.getDefaultInstance()
            )
                .setAudience(Collections.singletonList(BuildConfig.GOOGLE_CLIENT_ID))
                .build()

            val idToken = verifier.verify(googleCred.idToken)
                ?: throw Exception()

            idToken.payload
        }

        val snsLoginInfo = SNSUserInfo(
            socialId = payload.subject,
            snsType = SNSType.GOOGLE,
            email = payload.email,
            name = googleCred.givenName,
            nickname = googleCred.displayName
        )
        resultListener.onSuccess(snsLoginInfo)
    }
}