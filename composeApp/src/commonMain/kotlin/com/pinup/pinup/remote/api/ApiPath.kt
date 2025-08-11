package com.pinup.pinup.remote.api

object AuthPath{
    private const val AUTH = "api/auth"
    const val REFRESH = "$AUTH/refresh"
    const val SOCIAL_LOGIN = "$AUTH/social-login"
    const val EMAIL_LOGIN = "$AUTH/login"
    const val LOGOUT = "$AUTH/logout"

}

object MailPath {
    private const val EMAIL = "api/mail"
    const val VERIFY_CODE = "$EMAIL/verify-code"
    const val SEND_CODE = "$EMAIL/send-code"
}