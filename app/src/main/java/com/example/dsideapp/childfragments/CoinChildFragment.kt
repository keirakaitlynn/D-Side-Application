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
import android.graphics.Matrix
import android.view.animation.*
import android.view.animation.Transformation
import android.graphics.Camera
import android.view.animation.LinearInterpolator


class CoinChildFragment : Fragment() {

    private var coin: ImageView? = null
    private var btn: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var curSide = R.drawable.heads
        var isheads = true
        val v = inflater.inflate(R.layout.fragment_child_coin, container, false)
        coin = v.findViewById<View>(R.id.coin) as ImageView
        btn = v.findViewById<View>(R.id.btn) as Button
        btn!!.setOnClickListener {
            val stayTheSame = kotlin.random.Random.Default.nextBoolean()
            val animation: Rotate3dAnimation
            if (curSide == R.drawable.heads) {
                animation = Rotate3dAnimation(coin!!, R.drawable.heads, R.drawable.tails, 0f, 180f, 0f, 0f, 0f, 0f)
            } else {
                animation = Rotate3dAnimation(coin!!, R.drawable.tails, R.drawable.heads, 0f, 180f, 0f, 0f, 0f, 0f)
            }
            if (stayTheSame) {
                animation.repeatCount = 5 // must be odd (5+1 = 6 flips so the side will stay the same)
                if (isheads) {
                    System.out.println("heads to heads")
                    isheads = true
                    curSide = R.drawable.heads
                }
                else {
                    System.out.println("tails to Tails")
                    isheads = false
                    curSide = R.drawable.tails
                }
            } else {
                animation.repeatCount = 6 // must be even (6+1 = 7 flips so the side will not stay the same)
                if (isheads) {
                    System.out.println("heads to tails")
                    isheads = false
                    curSide = R.drawable.tails
                }
                else {
                    System.out.println("tails to heads")
                    isheads = true
                    curSide = R.drawable.heads
                }
            }
            animation.duration = 110
            animation.interpolator = LinearInterpolator()
            coin!!.startAnimation(animation)
        }
        return v
        }

    // IGNORE EVERYTHING BELOW THIS. CLASS ROTATE3DANIMATION IS SIMPLY TO FLIP THE COIN. IF YOU NEED TO **CHANGE** HOW COIN FLIPS, THEN LOOK BELOW AND PROCEED WITH CAUTION.
    class Rotate3dAnimation(
        private val imageView: ImageView,
        private var curDrawable: Int,
        private var nextDrawable: Int,
        private val fromXDegrees: Float,
        private val toXDegrees: Float,
        private val fromYDegrees: Float,
        private val toYDegrees: Float,
        private val fromZDegrees: Float,
        private val toZDegrees: Float
    ) :
        Animation() {
        private var camera: Camera? = null
        private var width = 0
        private var height = 0
        private var numOfRepetition = 0
        private var repeatCount = 0f
        override fun setRepeatCount(repeatCount: Int) {
            super.setRepeatCount(repeatCount)
            this.repeatCount = (repeatCount + 1).toFloat()
        }

        override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
            super.initialize(width, height, parentWidth, parentHeight)
            this.width = width / 2
            this.height = height / 2
            camera = Camera()
        }

        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            var xDegrees = fromXDegrees + (toXDegrees - fromXDegrees) * interpolatedTime
            val yDegrees = fromYDegrees + (toYDegrees - fromYDegrees) * interpolatedTime
            val zDegrees = fromZDegrees + (toZDegrees - fromZDegrees) * interpolatedTime
            val matrix: Matrix = t.matrix


            // ----------------- ZOOM ----------------- //
            if ((numOfRepetition + interpolatedTime) / (repeatCount / 2) <= 1) {
                imageView.scaleX = 1 + (numOfRepetition + interpolatedTime) / (repeatCount / 2)
                imageView.scaleY = 1 + (numOfRepetition + interpolatedTime) / (repeatCount / 2)
            } else if (numOfRepetition < repeatCount) {
                imageView.scaleX = 3 - (numOfRepetition + interpolatedTime) / (repeatCount / 2)
                imageView.scaleY = 3 - (numOfRepetition + interpolatedTime) / (repeatCount / 2)
            }


            // ----------------- ROTATE ----------------- //
            if (interpolatedTime >= 0.5f) {
                if (interpolatedTime == 1f) {
                    val temp = curDrawable
                    curDrawable = nextDrawable
                    nextDrawable = temp
                    numOfRepetition++
                } else {
                    imageView.setImageResource(nextDrawable)
                }
                xDegrees -= 180f
            } else if (interpolatedTime == 0f) {
                imageView.setImageResource(curDrawable)
            }
            camera!!.save()
            camera!!.rotateX(-xDegrees)
            camera!!.rotateY(yDegrees)
            camera!!.rotateZ(zDegrees)
            camera!!.getMatrix(matrix)
            camera!!.restore()
            matrix.preTranslate(-width.toFloat(), -height.toFloat())
            matrix.postTranslate(width.toFloat(), height.toFloat())
        }
    }


    companion object {
        val RANDOM = Random()
    }
}