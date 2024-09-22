package com.dev.indrivertrigger.repository

import com.dev.indrivertrigger.ui.authentication.model.SignInRequest
import com.dev.indrivertrigger.ui.authentication.model.SignInResponse
import com.dev.novisshop.network.RetrofitClient
import retrofit2.Response

class AuthRepository {

    suspend fun signIn(isHeader: Boolean, body: SignInRequest): Response<SignInResponse> {
        return RetrofitClient(isHeader).getApi().signIn(body)
    }

}