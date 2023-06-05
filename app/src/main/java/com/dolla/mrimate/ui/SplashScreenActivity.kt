package com.dolla.mrimate.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import com.dolla.mrimate.R
import com.dolla.mrimate.databinding.ActivitySplashScreenBinding
import com.dolla.mrimate.ui.activities.BaseActivity
import com.dolla.mrimate.ui.activities.OnboardingActivity
import com.dolla.mrimate.util.SPLASH_SCREEN_DELAY

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the content view of the activity to the root of the layout
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
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