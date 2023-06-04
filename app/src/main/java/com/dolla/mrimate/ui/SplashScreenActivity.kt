package com.dolla.mrimate.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.dolla.mrimate.R
import com.dolla.mrimate.databinding.ActivitySplashScreenBinding
import com.dolla.mrimate.ui.activities.OnboardingActivity
import com.dolla.mrimate.util.SPLASH_SCREEN_DELAY

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the content view of the activity to the root of the layout
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        ) // hide the status bar
    }

    override fun onStart() {
        super.onStart()
        navigateToOnboardingActivity()
    }

    private fun navigateToOnboardingActivity() {
        // start the animation of the logo
        binding.ivLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))

        // delay the transition to the next activity
        Handler(Looper.getMainLooper()).postDelayed({
            // start the next activity
            startActivity(
                Intent(
                    this,
                    OnboardingActivity::class.java
                )
            ) // TODO: change to RegistrationActivity::class.java
            // transition animation
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            // finish the current activity
            finish()
        }, SPLASH_SCREEN_DELAY)
    }
}