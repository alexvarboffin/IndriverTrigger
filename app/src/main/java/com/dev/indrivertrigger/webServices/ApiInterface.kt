package com.dev.indrivertrigger.webServices

import com.dev.indrivertrigger.ui.authentication.model.SignInRequest
import com.dev.indrivertrigger.ui.authentication.model.SignInResponse
import com.dev.indrivertrigger.webServices.ApiConstant.LOGIN
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiInterface {
    @POST(LOGIN)
    suspend fun signIn(@Body body: SignInRequest): Response<SignInResponse>

}