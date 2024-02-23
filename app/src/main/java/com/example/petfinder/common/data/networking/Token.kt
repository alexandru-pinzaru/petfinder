package com.example.petfinder.common.data.networking

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("token_type") val tokenType: String?,
    @SerializedName("expires_in") val expiresIn: Int?,
    @SerializedName("access_token") val accessToken: String?,
) {


    fun isValid(): Boolean {
        return !tokenType.isNullOrEmpty() &&
                expiresIn != null && expiresIn >= 0 && !accessToken.isNullOrEmpty()
    }

    companion object {
        val INVALID_TOKEN = Token("", -1, "")
        const val TOKEN_TYPE = "Bearer "
        const val AUTH_HEADER = "Authorization"
        const val GRANT_TYPE_KEY = "grant_type"
        const val GRANT_TYPE_VALUE = "client_credentials"
        const val CLIENT_ID = "client_id"
        const val CLIENT_SECRET = "client_secret"
    }
}