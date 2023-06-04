package com.dolla.mrimate.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.dolla.mrimate.util.onboardingImages

/**
 * @created 25/12/2022 - 5:33 PM
 * @project MRIMate
 * @author adell
 */
class OnboardingAdapter : PagerAdapter() {

    override fun getCount(): Int = onboardingImages.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(container.context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(onboardingImages[position])
        container.addView(imageView, 0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}