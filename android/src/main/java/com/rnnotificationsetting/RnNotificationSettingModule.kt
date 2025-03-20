package com.rnnotificationsetting

import android.app.Activity
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter

class RnNotificationSettingModule(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext), Application.ActivityLifecycleCallbacks,
    LifecycleEventListener {

    private val notificationManager: NotificationManager =
        reactContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        reactContext.addLifecycleEventListener(this)
        (reactContext.applicationContext as Application).registerActivityLifecycleCallbacks(this)
    }

    override fun getName(): String {
        return NAME
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @ReactMethod
    fun checkNotificationSettingsAndEmitEvent() {
        val areNotificationsEnabled = notificationManager.areNotificationsEnabled()
        val newNotificationStatus = if (areNotificationsEnabled) 1 else 0
        val params: WritableMap = WritableNativeMap()
        params.putInt("status", newNotificationStatus)
        sendEvent("NotificationSettingsChanged", params)
    }

    private fun sendEvent(eventName: String, params: WritableMap) {
        if (reactContext.hasActiveCatalystInstance()) {
            reactContext.getJSModule<RCTDeviceEventEmitter>(RCTDeviceEventEmitter::class.java)
                .emit(eventName, params)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun onAppStateChange() {
        checkNotificationSettingsAndEmitEvent()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResumed(activity: Activity) {
        onAppStateChange()
    }


    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onHostResume() {
    }

    override fun onHostPause() {
    }

    override fun onHostDestroy() {
    }

    companion object {
        const val NAME = "RnNotificationSetting"
    }
}
