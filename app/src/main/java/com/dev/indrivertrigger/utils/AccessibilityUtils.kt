package com.dev.indrivertrigger.utils

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.view.accessibility.AccessibilityManager

object AccessibilityUtils {

    /**
     * Checks if the Accessibility Service is enabled.
     *
     * @param context    Context to access system services.
     * @param serviceName Fully-qualified name of the Accessibility Service.
     * @return true if the service is enabled, false otherwise.
     */
    fun isAccessibilityServiceEnabled(context: Context, serviceName: String?): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        for (service in am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)) {
            val componentName = ComponentName.unflattenFromString(service.id)
            if (componentName != null && TextUtils.equals(
                    componentName.className,
                    serviceName
                )
            ) {
                return true
            }
        }
        return false
    }

    /**
     * Opens the Accessibility settings page.
     *
     * @param context Context to start the activity.
     */
    fun openAccessibilitySettings(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
