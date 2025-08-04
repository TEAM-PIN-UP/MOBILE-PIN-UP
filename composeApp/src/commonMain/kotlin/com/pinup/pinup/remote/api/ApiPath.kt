package com.pinup.pinup.remote.api

object AuthPath{
    private const val AUTH = "api/auth"
    const val REFRESH = "$AUTH/refresh"
    const val SOCIAL_LOGIN = "$AUTH/social-login"
    const val LOGIN = "$AUTH/login"
    const val LOGOUT = "$AUTH/logout"

}