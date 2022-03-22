package com.example.dsideapp.data

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.database.FirebaseDatabase

class RecyclerAdapterForTitles(val context: Context, val objectPoll: MutableList<PollObject>): RecyclerView.Adapter<RecyclerAdapterForTitles.ViewHolder>() {

    // NOTES: size of arrays of info to bind w/ ViewHolder must be equal
//    private var titles = arrayOf("Chapter One", "Chapter Two", "Chapter Three", "Chapter Five", "Chapter Six", "Chapter Seven", "Chapter Eight")
//    private var details = arrayOf("Item one details", "Item two details", "Item three details", "Item four details", "Item five details", "Item six details", "Item seven details", "Item eight details")
//    private var images = intArrayOf(R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1, R.drawable.card_1)
lateinit var  temp: View
lateinit var temp2: ViewGroup
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterForTitles.ViewHolder {
        // ViewHolder: the layout of the item to be duplicated/"recycled"
        temp = LayoutInflater.from(context).inflate(R.layout.polls_view, parent, false)
        temp2 = parent
        return ViewHolder(temp)
    }

    override fun getItemCount(): Int {
        // the total # of items you want to display to pass to ViewHolder
        return objectPoll.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapterForTitles.ViewHolder, position: Int) {
        // Iterates through an array (cart, images, etc).
        // Binds ViewHolder's vars to RecyclerAdapter's vars (cart_activity_card.xml w/ RecyclerAdapter.kt).
        val title = objectPoll[position]
        holder.bind(title)

        // NOTES: size of arrays of info to bind w/ ViewHolder must be equal
        // NOTES: Line below will crash because images[].size != cart.size (or "getItemCount()")
        //holder.itemImage.setImageResource(images[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("RestrictedApi")
        fun bind(p: PollObject){
            lateinit var v: View
            var itemImage: ImageView
            var itemTitle: Button
            var layoutThingy = R.layout.fragment_vote2_poll

            itemImage = itemView.findViewById(R.id.user_image)
            itemTitle = itemView.findViewById(R.id.poll_title)

            itemTitle.text = p.business_name
            //Getting database info
            var authorization = auth
            var user = authorization.currentUser
            var userID = authorization.currentUser?.uid
            var db = FirebaseDatabase.getInstance().getReference()

            //Giving Voting functionality to the polls
            itemTitle.setOnClickListener{
                val numOfOptions = p.poll_options?.size
                //Setting up popup
                // inflate the layout of the popup window with correct vote view
                // create the popup window
                val width = LinearLayout.LayoutParams.WRAP_CONTENT
                val height = LinearLayout.LayoutParams.WRAP_CONTENT
                val focusable = true // lets taps outside the popup also dismiss it
                //
                if (numOfOptions == 6){
                    v = LayoutInflater.from(context).inflate(R.layout.fragment_vote6_poll, null )
                    layoutThingy = R.layout.fragment_vote6_poll
                    val b6 = v.findViewById<Button>(R.id.button6)
                    b6.text = p.poll_options[5]
                    b6.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt6Vote")
                            .setValue(""+(p.poll_vote_count!![5]+1))
                    }
                    //
                    val b5 = v.findViewById<Button>(R.id.button5)
                    b5.text = p.poll_options[4]
                    b5.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt5Vote")
                            .setValue(""+(p.poll_vote_count!![4]+1))
                    }
                    //
                    val b4 = v.findViewById<Button>(R.id.button4)
                    b4.text = p.poll_options[3]
                    b4.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt4Vote")
                            .setValue(""+(p.poll_vote_count!![3]+1))
                    }
                    //
                    val b3 = v.findViewById<Button>(R.id.button3)
                    b3.text = p.poll_options[2]
                    b3.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt3Vote")
                            .setValue(""+(p.poll_vote_count!![2]+1))
                    }
                    //
                    val b2 = v.findViewById<Button>(R.id.button2)
                    b2.text = p.poll_options[1]
                    b2.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                            .setValue(""+(p.poll_vote_count!![1]+1))
                    }
                    //
                    val b1 = v.findViewById<Button>(R.id.button)
                    b1.text = p.poll_options[0]
                    b1.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                            .setValue(""+(p.poll_vote_count!![0]+1))
                    }
                    //
                }
                else if (numOfOptions == 5){
                    v = LayoutInflater.from(context).inflate(R.layout.fragment_vote5_poll, null)
                    layoutThingy = R.layout.fragment_vote5_poll
                    val b5 = v.findViewById<Button>(R.id.button5)
                    b5.text = p.poll_options[4]
                    b5.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt5Vote")
                            .setValue(""+(p.poll_vote_count!![4]+1))
                    }
                    //
                    val b4 = v.findViewById<Button>(R.id.button4)
                    b4.text = p.poll_options[3]
                    b4.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt4Vote")
                            .setValue(""+(p.poll_vote_count!![3]+1))
                    }
                    //
                    val b3 = v.findViewById<Button>(R.id.button3)
                    b3.text = p.poll_options[2]
                    b3.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt3Vote")
                            .setValue(""+(p.poll_vote_count!![2]+1))
                    }
                    //
                    val b2 = v.findViewById<Button>(R.id.button2)
                    b2.text = p.poll_options[1]
                    b2.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                            .setValue(""+(p.poll_vote_count!![1]+1))
                    }
                    //
                    val b1 = v.findViewById<Button>(R.id.button)
                    b1.text = p.poll_options[0]
                    b1.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                            .setValue(""+(p.poll_vote_count!![0]+1))
                    }
                    //
                }
                else if (numOfOptions == 4){
                    v = LayoutInflater.from(context).inflate(R.layout.fragment_vote4_poll, null)
                    layoutThingy = R.layout.fragment_vote4_poll
                    val b4 = v.findViewById<Button>(R.id.button4)
                    b4.text = p.poll_options[3]
                    b4.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt4Vote")
                            .setValue(""+(p.poll_vote_count!![3]+1))
                    }
                    //
                    val b3 = v.findViewById<Button>(R.id.button3)
                    b3.text = p.poll_options[2]
                    b3.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt3Vote")
                            .setValue(""+(p.poll_vote_count!![2]+1))
                    }
                    //
                    val b2 = v.findViewById<Button>(R.id.button2)
                    b2.text = p.poll_options[1]
                    b2.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                            .setValue(""+(p.poll_vote_count!![1]+1))
                    }
                    //
                    val b1 = v.findViewById<Button>(R.id.button)
                    b1.text = p.poll_options[0]
                    b1.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                            .setValue(""+(p.poll_vote_count!![0]+1))
                    }
                    //
                }
                else if (numOfOptions == 3){
                    v = LayoutInflater.from(context).inflate(R.layout.fragment_vote3_poll, null)
                    layoutThingy = R.layout.fragment_vote3_poll
                    val b3 = v.findViewById<Button>(R.id.button3)
                    b3.text = p.poll_options[2]
                    b3.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt3Vote")
                            .setValue(""+(p.poll_vote_count!![2]+1))
                    }
                    //
                    val b2 = v.findViewById<Button>(R.id.button2)
                    b2.text = p.poll_options[1]
                    b2.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                            .setValue(""+(p.poll_vote_count!![1]+1))
                    }
                    //
                    val b1 = v.findViewById<Button>(R.id.button)
                    b1.text = p.poll_options[0]
                    b1.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                            .setValue(""+(p.poll_vote_count!![0]+1))
                    }
                    //
                }
                else{
                    v = LayoutInflater.from(context).inflate(R.layout.fragment_vote2_poll, null)
                    layoutThingy = R.layout.fragment_vote2_poll
                    val b2 = v.findViewById<Button>(R.id.button2)
                    b2.text = p.poll_options!![1]
                    b2.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                            .setValue(""+(p.poll_vote_count!![1]+1))
                    }
                    //
                    val b1 = v.findViewById<Button>(R.id.button)
                    b1.text = p.poll_options[0]
                    b1.setOnClickListener{
                        db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                            .setValue(""+(p.poll_vote_count!![0]+1))
                    }
                    //
                }


                var popupView = LayoutInflater.from(getActivity(context)).inflate(layoutThingy, null);
                var popupWindow = PopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

//                val popupWindow = PopupWindow(itemView, width, height, focusable)
                // show the popup window
//                popupWindow.showAtLocation(temp, Gravity.CENTER, 0, 0)
            }
        }
    }
}
