package com.example.petfinder.common.data.networking

import android.text.format.DateUtils
import com.example.petfinder.common.data.cache.CacheManager
import com.example.petfinder.common.data.networking.Constants.API_KEY
import com.example.petfinder.common.data.networking.Constants.API_SECRET
import com.example.petfinder.common.data.networking.Constants.AUTH_URL
import com.example.petfinder.common.data.networking.Token.Companion.AUTH_HEADER
import com.example.petfinder.common.data.networking.Token.Companion.CLIENT_ID
import com.example.petfinder.common.data.networking.Token.Companion.CLIENT_SECRET
import com.example.petfinder.common.data.networking.Token.Companion.GRANT_TYPE_KEY
import com.example.petfinder.common.data.networking.Token.Companion.GRANT_TYPE_VALUE
import com.example.petfinder.common.data.networking.Token.Companion.TOKEN_TYPE
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(
    private val cacheManager: CacheManager,
    private val gson: Gson,
) : Interceptor {

    private val AUTH_TOKEN_KEY = "AUTH_TOKEN"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val interceptedRequest: Request

        if (cacheManager.hadExpired(AUTH_TOKEN_KEY)) {
            interceptedRequest = chain.createAuthenticatedRequest(
                cacheManager.getObjectUnexpired(
                    AUTH_TOKEN_KEY,
                    Token::class.java,
                    Token.INVALID_TOKEN
                )
                    .accessToken!!
            )
        } else {
            val tokenRefreshResponse = chain.refreshToken()
            interceptedRequest = if (tokenRefreshResponse.isSuccessful) {
                val newToken = mapToken(tokenRefreshResponse)
                if (newToken.isValid()) {
                    storeNewToken(newToken)
                    chain.createAuthenticatedRequest(newToken.accessToken!!)
                } else {
                    request
                }
            } else {
                request
            }
        }
        return chain.proceedDeletingTokenIfUnauthorized(interceptedRequest)
    }

    private fun Interceptor.Chain.createAuthenticatedRequest(token: String): Request {
        return request()
            .newBuilder()
            .addHeader(AUTH_HEADER, TOKEN_TYPE + token)
            .build()
    }

    private fun Interceptor.Chain.refreshToken(): Response {
        val url = request()
            .url
            .newBuilder(AUTH_URL)!!
            .build()

        val body = FormBody.Builder()
            .add(GRANT_TYPE_KEY, GRANT_TYPE_VALUE)
            .add(CLIENT_ID, API_KEY)
            .add(CLIENT_SECRET, API_SECRET)
            .build()

        val tokenRefresh = request()
            .newBuilder()
            .post(body)
            .url(url)
            .build()

        return proceedDeletingTokenIfUnauthorized(tokenRefresh)
    }

    private fun Interceptor.Chain.proceedDeletingTokenIfUnauthorized(request: Request): Response {
        val response = proceed(request)

        if (response.code == UNAUTHORIZED) {
            cacheManager.deleteObject(AUTH_TOKEN_KEY)
        }
        return response
    }

    private fun mapToken(tokenRefreshResponse: Response): Token {
        return gson.fromJson(tokenRefreshResponse.body.string(), Token::class.java)
            ?: Token.INVALID_TOKEN
    }

    private fun storeNewToken(token: Token) {
        cacheManager.putObject(
            AUTH_TOKEN_KEY,
            token,
            token.expiresIn!! * DateUtils.MINUTE_IN_MILLIS
        )
    }

    companion object {
        const val UNAUTHORIZED = 401
    }
}