package com.dolla.mrimate.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

/**
 * @created 26/12/2022 - 4:37 PM
 * @project MRIMate
 * @author adell
 */

fun Activity.makeStatusBarAndNavigationBarTransparent() {
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        // addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        // clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)


        decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                // or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                // or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                )
        statusBarColor = Color.TRANSPARENT

    }
}

fun AppCompatActivity.setContentViewCustom(view: View) {
    setContentView(view)
    makeStatusBarAndNavigationBarTransparent()
}

fun Activity.hideSystemUI() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.hide(WindowInsets.Type.statusBars())
        window.insetsController?.hide(WindowInsets.Type.navigationBars())
        window.insetsController?.systemBarsBehavior =
            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                )
        window.statusBarColor = Color.TRANSPARENT
    }
}
