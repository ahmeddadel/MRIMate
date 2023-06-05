package com.dolla.mrimate.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.dolla.mrimate.R
import com.dolla.mrimate.adapters.OnboardingAdapter
import com.dolla.mrimate.databinding.ActivityOnboardingBinding
import com.dolla.mrimate.util.onboardingBodies
import com.dolla.mrimate.util.onboardingTitles

class OnboardingActivity : BaseActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the content view of the activity to the root of the layout
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        setUpOnboardingFirstScreen()
        setUpViewPager()
    }

    // set text and button behavior for the first screen
    private fun setUpOnboardingFirstScreen() {
        binding.tvOnboardingTitle.text = getText(onboardingTitles[0])
        binding.tvOnboardingBody.text = getText(onboardingBodies[0])
        binding.btGetStarted.text = getText(R.string.next)
        binding.btGetStarted.setOnClickListener { binding.viewpagerOnboarding.currentItem++ }
        binding.tvSkip.setOnClickListener { navigateToRegistrationActivity() }
    }

    private fun setUpViewPager() {
        // set the view pager adapter
        binding.viewpagerOnboarding.adapter = OnboardingAdapter()
        // set the view pager page change listener
        binding.viewpagerOnboarding.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                // do nothing
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // do nothing
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> onScreenSwiped(onboardingTitles[0], onboardingBodies[0])
                    1 -> onScreenSwiped(onboardingTitles[1], onboardingBodies[1])
                    2 -> onScreenSwiped(onboardingTitles[2], onboardingBodies[2], true)
                }
            }
        })
    }

    // set the title and body text of the onboarding screen
    private fun onScreenSwiped(title: Int, body: Int, lastScreen: Boolean = false) {
        binding.tvOnboardingTitle.text = getText(title)
        binding.tvOnboardingBody.text = getText(body)
        if (lastScreen) {
            binding.btGetStarted.text = getText(R.string.get_started)
            binding.btGetStarted.setOnClickListener { navigateToRegistrationActivity() }
            binding.tvSkip.visibility = View.GONE
        } else {
            binding.btGetStarted.text = getText(R.string.next)
            binding.btGetStarted.setOnClickListener { binding.viewpagerOnboarding.currentItem++ }
            binding.tvSkip.visibility = View.VISIBLE
        }
    }

    // navigate to the registration activity and finish this activity
    private fun navigateToRegistrationActivity() {
        startActivity(Intent(this, RegistrationActivity::class.java))
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        finish()
    }
}
