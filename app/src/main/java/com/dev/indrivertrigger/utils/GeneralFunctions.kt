package com.dev.noviswarehouse

import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.dev.indrivertrigger.Myapp.Companion.appContext
import com.dev.indrivertrigger.R
import okhttp3.ResponseBody
import org.json.JSONObject
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


object GeneralFunctions {

    fun getError(throwable: Throwable): String {
        return when (throwable) {
            is UnknownHostException -> "No Internet Connection"
            is SocketTimeoutException -> "Server is not responding. Please try again"
            is ConnectException -> "Failed to connect server"
            else -> "something went wrong !! please try again"
        }
    }

    fun extractErrorMessage(errorBody: ResponseBody?): JSONObject? {
        return errorBody?.string()?.let { JSONObject(it) }
    }


    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target!!).matches()
    }

    /*fun hideShowPassword(editText: TextInputEditText, imageView: ShapeableImageView) {
        if (editText.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
            imageView.setImageResource(R.drawable.ic_eye_open)
            //show password
            editText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            editText.setSelection(editText.length())
        } else {
            imageView.setImageResource(R.drawable.ic_eye_close)
            //Hide Password
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            editText.setSelection(editText.length())
        }
    }*/

    fun showToast(message: String) {
        Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
    }

    fun showInternetToast() {
        Toast.makeText(
            appContext,
            appContext.getString(R.string.check_your_network_connection),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun getTimeFromLong(simpleDateFormat: SimpleDateFormat, time: Long?): String {
        val date = Date()
        date.time = time!!
        simpleDateFormat.timeZone = TimeZone.getDefault()
        return simpleDateFormat.format(date)
    }

    fun editTextValidation(editText: View, error: String?): Boolean {
        if ((editText as TextView).text.toString().trim { it <= ' ' }.isEmpty()) {
            editText.error = error
            editText.requestFocus()
            return false
        }
        return true
    }


}