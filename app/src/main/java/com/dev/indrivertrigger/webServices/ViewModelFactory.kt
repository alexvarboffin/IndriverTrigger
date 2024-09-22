package com.dev.indrivertrigger.webServices

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dev.indrivertrigger.repository.AuthRepository
import com.dev.indrivertrigger.viewmodels.AuthViewModel

class ViewModelFactory(val application: Application, private  val repository: AuthRepository):
    ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository) as T
            }
            else -> throw  IllegalArgumentException("Class not found")
        }
    }
}

