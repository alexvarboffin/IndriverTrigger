package com.dev.indrivertrigger.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.indrivertrigger.repository.AuthRepository
import com.dev.indrivertrigger.ui.authentication.model.SignInRequest
import com.dev.indrivertrigger.ui.authentication.model.SignInResponse
import com.dev.indrivertrigger.utils.ConstantValues.INTERNAL_SERVER_ERROR
import com.dev.indrivertrigger.utils.ConstantValues.STATUS_BAD_REQUEST
import com.dev.indrivertrigger.utils.ConstantValues.STATUS_OK
import com.dev.indrivertrigger.utils.ConstantValues.STATUS_SERVER_ERROR
import com.dev.indrivertrigger.utils.ConstantValues.STATUS_UNAUTHORIZED
import com.dev.indrivertrigger.webServices.Resource
import com.dev.noviswarehouse.GeneralFunctions.extractErrorMessage
import com.dev.noviswarehouse.GeneralFunctions.getError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(private var repository: AuthRepository) : ViewModel() {
    var signIn = MutableLiveData<Resource<SignInResponse>>()

    fun onSignInUser(isHeader: Boolean, model: SignInRequest) {
        signIn.value = Resource.loading(data = null)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.signIn(isHeader, model)
                withContext(Dispatchers.Main)
                {
                    if (response.code() == STATUS_OK) {
                        if (response.body() != null) {
                            signIn.value = Resource.success(
                                data = response.body()!!,
                                message = response.message()
                            )
                        }
                    } else if (response.code() == STATUS_BAD_REQUEST) {
                        signIn.value = Resource.error(
                            data = null,
                            message = extractErrorMessage(response.errorBody())!!.getString("message")
                        )
                    } else if (response.code() == STATUS_UNAUTHORIZED) {
//                        logoutUser(appContext)
                        signIn.value = Resource.error(data = null, message = extractErrorMessage(response.errorBody())!!.getString("message"))
                    } else if (response.code() == STATUS_SERVER_ERROR) {
                        signIn.value = Resource.error(
                            data = null,
                            message = INTERNAL_SERVER_ERROR
                        )
                    } else {
                        signIn.value = Resource.error(data = null, message = response.message())
                    }
                }
            } catch (throwable: Throwable) {
                withContext(Dispatchers.Main)
                {
                    signIn.value = Resource.error(data = null, message = getError(throwable))
                }
            }
        }
    }


}

