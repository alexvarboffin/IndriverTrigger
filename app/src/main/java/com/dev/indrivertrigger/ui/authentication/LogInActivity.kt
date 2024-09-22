package com.dev.indrivertrigger.ui.authentication

import ProgressDialogUtils
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dev.indrivertrigger.MainActivity2
import com.dev.indrivertrigger.R
import com.dev.indrivertrigger.repository.AuthRepository
import com.dev.indrivertrigger.ui.authentication.model.SignInRequest
import com.dev.indrivertrigger.utils.ConstantValues.NAME
import com.dev.indrivertrigger.utils.ConstantValues.PHONE
import com.dev.indrivertrigger.utils.ConstantValues.TOKEN
import com.dev.indrivertrigger.utils.ConstantValues.USER_ID
import com.dev.indrivertrigger.utils.PreferenceHelper
import com.dev.indrivertrigger.utils.PreferenceHelper.set
import com.dev.indrivertrigger.viewmodels.AuthViewModel
import com.dev.indrivertrigger.webServices.Status
import com.dev.indrivertrigger.webServices.ViewModelFactory
import com.dev.noviswarehouse.GeneralFunctions
import com.dev.noviswarehouse.GeneralFunctions.showToast

class LogInActivity : AppCompatActivity() {

    private lateinit var viewModelFactory: ViewModelFactory
    private var authViewModel: AuthViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        viewModelFactory = ViewModelFactory(Application(), AuthRepository())
        authViewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]

        setObserver()
        val phoneNumber = findViewById<EditText>(R.id.phoneNumberEt)
        val password = findViewById<EditText>(R.id.passwordEt)
        val logInBtn = findViewById<Button>(R.id.logInBtn)

        logInBtn.setOnClickListener {
           if(phoneNumber.text.isEmpty()){
               showToast("Please enter email")
               return@setOnClickListener
           }

            if(!GeneralFunctions.isValidEmail(phoneNumber.text.toString())) {
                showToast("Please enter valid email")
                return@setOnClickListener
            }
            if(password.text.toString().trim().isEmpty()) {
                showToast("Please enter your password")
                return@setOnClickListener
            }

            authViewModel?.onSignInUser(
                false, SignInRequest(
                    phoneNumber.text.toString().trim(),
                    password.text.toString().trim()
                )
            )
        }

    }


    private fun setObserver() {
        authViewModel?.signIn?.observe(this@LogInActivity) { resource ->
            Log.d("checkObserver", "setObserver: Inside observer")
            Log.d("checkObserver", "setObserver: ${resource.status}")
            when (resource.status) {
                Status.SUCCESS -> {
                    ProgressDialogUtils.hideProgressDialog()

                    val data = resource.data!!

                    PreferenceHelper.defaultPrefs(this@LogInActivity)[TOKEN] = data.token
                    PreferenceHelper.defaultPrefs(this@LogInActivity)[USER_ID] = data.user.id
                    PreferenceHelper.defaultPrefs(this@LogInActivity)[NAME] = data.user.name
                    PreferenceHelper.defaultPrefs(this@LogInActivity)[PHONE] = data.user.phone

                    startActivity(
                        Intent(this@LogInActivity, MainActivity2::class.java)
                    )
                }

                Status.LOADING -> {
                    ProgressDialogUtils.showProgressDialog(this@LogInActivity)
                }

                Status.ERROR -> {
                    ProgressDialogUtils.hideProgressDialog()
                    showToast(resource.message.toString())
                }
            }
        }

    }


}