package com.example.dsideapp.childfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import java.util.*
import android.view.animation.*

class CoinChildFragment : Fragment() {

    private var coin: ImageView? = null
    private var btn: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_child_coin, container, false)
        coin = v.findViewById<View>(R.id.coin) as ImageView
        btn = v.findViewById<View>(R.id.btn) as Button
        btn!!.setOnClickListener { flipCoin() }
        return v
        }

    private fun flipCoin() {
        val fadeOut: Animation = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = AccelerateInterpolator()
        fadeOut.duration = 1000
        fadeOut.fillAfter = true
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                coin?.setImageResource(if (RANDOM.nextFloat() > 0.5f) R.drawable.tails else R.drawable.heads)
                val fadeIn: Animation = AlphaAnimation(0f, 1f)
                fadeIn.interpolator = DecelerateInterpolator()
                fadeIn.duration = 3000
                fadeIn.fillAfter = true
                coin!!.startAnimation(fadeIn)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        coin!!.startAnimation(fadeOut)
    }

    companion object {
        val RANDOM = Random()
    }
}