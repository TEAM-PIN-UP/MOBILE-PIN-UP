package com.pinup.placePinup.platform

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
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
import org.koin.java.KoinJavaComponent
import java.util.Collections

actual class GoogleLoginController : SNSLoginController {
    override fun doLogin(resultListener: SNSLoginResultListener) {
        CoroutineScope(Dispatchers.IO).launch {
            val context: Context = KoinJavaComponent.getKoin().get()
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .build()

            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            try {
                val result = CredentialManager.create(context)
                    .getCredential(
                        context = context,
                        request = request
                    )

                // start credential
                val credentials = result.credential
                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credentials.data)

                val googleIdToken = googleIdTokenCredential.idToken
                val verifier = GoogleIdTokenVerifier.Builder(
                    NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
                )
                    .setAudience(Collections.singletonList(BuildConfig.GOOGLE_CLIENT_ID))
                    .build()
                val idToken = withContext(Dispatchers.IO) {
                    verifier.verify(googleIdToken)
                }
                val payload = idToken.payload
                val snsLoginInfo = SNSUserInfo(
                    socialId = payload.subject,
                    snsType = SNSType.GOOGLE,
                    email = payload.email,
                    name = googleIdTokenCredential.givenName,
                    nickname = googleIdTokenCredential.displayName
                )
                resultListener.onSuccess(snsLoginInfo)
            } catch (e: Exception) {
                resultListener.onCancel()
            }
        }
    }
}