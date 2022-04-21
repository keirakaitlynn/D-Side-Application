package com.example.dsideapp.childfragments

import android.graphics.Camera
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import nl.dionsegijn.konfetti.xml.KonfettiView
import com.example.dsideapp.data.Effects
import com.example.dsideapp.data.selectedItemsForDecisionTools

class CoinChildFragment : Fragment() {

    private lateinit var viewKonfetti: KonfettiView
    private var coin: ImageView? = null
    private var btn: Button? = null
    private lateinit var viewOfLayout: View
    private var isHeads = false
    //getting database info
    var authorization = auth
    var user = authorization.currentUser
    var userID = authorization.currentUser?.uid
    var db = FirebaseDatabase.getInstance().getReference()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_child_coin, container, false)
        var activityList = mutableListOf<String>()
        var activitesOnLeftScreen = ""
        var activitesOnRightScreen = ""
        var numberForActivitesOnLeftScreen = ""
        var numberForActivitesOnRightScreen = ""
        viewKonfetti = v.findViewById(R.id.konfettiView)

        v.findViewById<TextView>(R.id.Result).visibility = View.GONE
        ///Grabbing all the activities from DB to populate screen
        //Populate an array to with DB Cart activities
        var activityInfo =
            db.child("users").child(userID.toString()).get().addOnSuccessListener {
                if (it.exists()) {
                    val allTheStuff = selectedItemsForDecisionTools
                    allTheStuff.forEach { (key, value) ->
                        //Putting activities on top left or right of the screen
                        var tempName = key.value.toString().substringAfter("title=")
                            .substringBefore("image_type=").trim()
                        if (activityList.size < 1) {
                            if (value == 1){
                                activitesOnLeftScreen += "" + tempName + "\n"
                                numberForActivitesOnLeftScreen += "" + (activityList.size + 1)
                            }
                        } else if (activityList.size < 2){
                            if (value == 2){
                                activitesOnRightScreen += "" + tempName + "\n"
                                numberForActivitesOnRightScreen += "" + (activityList.size + 1)
                            }
                        }
                        if (activityList.size < 2) {
                            activityList.add(tempName)
                        }
                    }
                }
                //Display activities on screen
                //Setting the text views with the activites listed
                Log.w("XD", "left" + activitesOnLeftScreen)
                Log.w("XD", "Right" + activitesOnRightScreen)
                v.findViewById<TextView>(R.id.left_activities_coin).setText(activitesOnLeftScreen)
                v.findViewById<TextView>(R.id.number_for_left_activities_coin).setText(numberForActivitesOnLeftScreen)
                v.findViewById<TextView>(R.id.right_activities_coin).setText(activitesOnRightScreen)
                v.findViewById<TextView>(R.id.number_for_right_activities_coin).setText(numberForActivitesOnRightScreen)
                //v.findViewById<TextView>(R.id.right_activities_coin).setText(activitesOnRightScreen)
                Log.w("XD", activitesOnLeftScreen)
            }
        var curSide = R.drawable.heads
        var isheads = true
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



            isHeads = isheads
            animation.duration = 110
            animation.interpolator = LinearInterpolator()
            coin!!.startAnimation(animation)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(arg0: Animation) {
                    v.findViewById<TextView>(R.id.Result).text = ""
                    v.findViewById<TextView>(R.id.Result).visibility = View.GONE
                }
                override fun onAnimationRepeat(arg0: Animation) {}
                override fun onAnimationEnd(arg0: Animation) {

                    val sol = listOf(activityList[0],activityList[1])
                    var solution = if (isHeads.toString() == "true")  "heads: " + sol[0] else "tails: " + sol[1]
                    Log.w("RAWR ", solution )

                    v.findViewById<TextView>(R.id.Result).text = solution
                    v.findViewById<TextView>(R.id.Result).visibility = View.VISIBLE
                    explode()
                }
            })

        }
        return v
    }

    fun clearTheCart(){
        var authorization = auth
        var user = authorization.currentUser
        var userID = authorization.currentUser?.uid
        var db = FirebaseDatabase.getInstance().getReference()
        // db.child("users").child(userID.toString()).child("data").child("cart").
    }

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
            //System.err.println(interpolatedTime)
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

    private fun explode() {
        viewKonfetti.start(Effects.explode())
    }
}

/*package com.example.dsideapp.childfragments
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
}*/