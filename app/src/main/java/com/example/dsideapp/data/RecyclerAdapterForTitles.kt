package com.example.dsideapp.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.dsideapp.R
import com.example.dsideapp.auth
import com.example.dsideapp.fragments.PollsFragment
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.appcompat.app.AppCompatActivity




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
            var numOfOptions = 0
            var iterations = 0
            //Giving Voting functionality to the polls
            itemTitle.setOnClickListener{
                p.poll_options?.forEach { optionString ->
                    if (iterations==0){
                        if (!optionString.equals("Option 1")){
                            numOfOptions += 1
                        }
                    }
                    else if (iterations==1){
                        if (!optionString.equals("Option 2")){
                            numOfOptions += 1
                        }
                    }
                    else if (iterations==2){
                        if (!optionString.equals("Option 3")){
                            numOfOptions += 1
                        }
                    }
                    else if (iterations==3){
                        if (!optionString.equals("Option 4")){
                            numOfOptions += 1
                        }
                    }
                    else if (iterations==4){
                        if (!optionString.equals("Option 5")){
                            numOfOptions += 1
                        }
                    }
                    else if (iterations==5){
                        if (!optionString.equals("Option 6")){
                            numOfOptions += 1
                        }
                    }
                    iterations += 1
                    //Log.w("", "THE NUM COUNT IS " + numOfOptions)
                }
                //Setting up popup
                // inflate the layout of the popup window with correct vote view
                // create the popup window
                val width = LinearLayout.LayoutParams.WRAP_CONTENT
                val height = LinearLayout.LayoutParams.WRAP_CONTENT
                val focusable = true // lets taps outside the popup also dismiss it
                //
                var dbVoters = p.voters
                fun refreshTheFragment(){
                    val manager = (context as AppCompatActivity).supportFragmentManager
                    Log.w("HERE BE I: ", manager.toString())
                    if (manager != null) {
                        manager.beginTransaction().replace(com.example.dsideapp.R.id.fragment_view,  PollsFragment()).commit()
                        Log.w("Made it here","!")
                    }
                }
                //Log.w("", p.poll_options.toString())
                if (dbVoters?.split(',')?.contains(user?.email.toString()) == false) {

                    if (numOfOptions == 6) {
                        v = LayoutInflater.from(context).inflate(R.layout.fragment_vote6_poll, null)
                        layoutThingy = R.layout.fragment_vote6_poll
                        val b6 = v.findViewById<Button>(R.id.button6)
                        b6.text = p.poll_options!![5]
                        val b1 = v.findViewById<Button>(R.id.button)
                        b1.text = p.poll_options[0]
                        val b2 = v.findViewById<Button>(R.id.button2)
                        b2.text = p.poll_options[1]
                        val b3 = v.findViewById<Button>(R.id.button3)
                        b3.text = p.poll_options[2]
                        val b4 = v.findViewById<Button>(R.id.button4)
                        b4.text = p.poll_options[3]
                        val b5 = v.findViewById<Button>(R.id.button5)
                        b5.text = p.poll_options[4]
                        var popupView =
                            LayoutInflater.from(getActivity(context)).inflate(layoutThingy, null);
                        var popupWindow = PopupWindow(
                            v,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT
                        );
                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                        b6.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt6Vote")
                                .setValue("" + (p.poll_vote_count!![5] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b5.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt5Vote")
                                .setValue("" + (p.poll_vote_count!![4] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b4.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt4Vote")
                                .setValue("" + (p.poll_vote_count!![3] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b3.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt3Vote")
                                .setValue("" + (p.poll_vote_count!![2] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b2.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                                .setValue("" + (p.poll_vote_count!![1] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b1.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                                .setValue("" + (p.poll_vote_count!![0] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //
                    } else if (numOfOptions == 5) {
                        v = LayoutInflater.from(context).inflate(R.layout.fragment_vote5_poll, null)
                        layoutThingy = R.layout.fragment_vote5_poll
                        val b5 = v.findViewById<Button>(R.id.button5)
                        b5.text = p.poll_options!![4]
                        val b1 = v.findViewById<Button>(R.id.button)
                        b1.text = p.poll_options[0]
                        val b2 = v.findViewById<Button>(R.id.button2)
                        b2.text = p.poll_options[1]
                        val b3 = v.findViewById<Button>(R.id.button3)
                        b3.text = p.poll_options[2]
                        val b4 = v.findViewById<Button>(R.id.button4)
                        b4.text = p.poll_options[3]
                        var popupView =
                            LayoutInflater.from(getActivity(context)).inflate(layoutThingy, null);
                        var popupWindow = PopupWindow(
                            v,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT
                        );
                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                        b5.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt5Vote")
                                .setValue("" + (p.poll_vote_count!![4] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b4.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt4Vote")
                                .setValue("" + (p.poll_vote_count!![3] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b3.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt3Vote")
                                .setValue("" + (p.poll_vote_count!![2] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b2.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                                .setValue("" + (p.poll_vote_count!![1] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b1.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                                .setValue("" + (p.poll_vote_count!![0] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //
                    } else if (numOfOptions == 4) {
                        v = LayoutInflater.from(context).inflate(R.layout.fragment_vote4_poll, null)
                        layoutThingy = R.layout.fragment_vote4_poll
                        val b4 = v.findViewById<Button>(R.id.button4)
                        b4.text = p.poll_options!![3]
                        val b1 = v.findViewById<Button>(R.id.button)
                        b1.text = p.poll_options[0]
                        val b2 = v.findViewById<Button>(R.id.button2)
                        b2.text = p.poll_options[1]
                        val b3 = v.findViewById<Button>(R.id.button3)
                        b3.text = p.poll_options[2]
                        var popupView =
                            LayoutInflater.from(getActivity(context)).inflate(layoutThingy, null);
                        var popupWindow = PopupWindow(
                            v,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT
                        );
                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                        b4.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt4Vote")
                                .setValue("" + (p.poll_vote_count!![3] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b3.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt3Vote")
                                .setValue("" + (p.poll_vote_count!![2] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b2.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                                .setValue("" + (p.poll_vote_count!![1] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b1.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                                .setValue("" + (p.poll_vote_count!![0] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //
                    } else if (numOfOptions == 3) {
                        v = LayoutInflater.from(context).inflate(R.layout.fragment_vote3_poll, null)
                        layoutThingy = R.layout.fragment_vote3_poll
                        val b3 = v.findViewById<Button>(R.id.button3)
                        b3.text = p.poll_options!![2]
                        val b1 = v.findViewById<Button>(R.id.button)
                        b1.text = p.poll_options[0]
                        val b2 = v.findViewById<Button>(R.id.button2)
                        b2.text = p.poll_options[1]
                        var popupView =
                            LayoutInflater.from(getActivity(context)).inflate(layoutThingy, null);
                        var popupWindow = PopupWindow(
                            v,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT
                        );
                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                        b3.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt3Vote")
                                .setValue("" + (p.poll_vote_count!![2] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b2.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                                .setValue("" + (p.poll_vote_count!![1] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b1.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                                .setValue("" + (p.poll_vote_count!![0] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //
                    } else {
                        v = LayoutInflater.from(context).inflate(R.layout.fragment_vote2_poll, null)
                        layoutThingy = R.layout.fragment_vote2_poll
                        val b2 = v.findViewById<Button>(R.id.button2)
                        b2.text = p.poll_options!![1]
                        val b1 = v.findViewById<Button>(R.id.button)
                        b1.text = p.poll_options[0]
                        var popupView =
                            LayoutInflater.from(getActivity(context)).inflate(layoutThingy, null);
                        var popupWindow = PopupWindow(
                            v,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT
                        );
                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                        b2.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                                .setValue("" + (p.poll_vote_count!![1] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )
                        }
                        //

                        b1.setOnClickListener {
                            db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                                .setValue("" + (p.poll_vote_count!![0] + 1))
                            refreshTheFragment()
                            popupWindow.dismiss()
                            true
                            db.child("Public Polls").child(p.id.toString()).child("voters")
                                .setValue(
                                    dbVoters + user?.email.toString() + ","
                                )

                        }
                        //
                    }
                }
                else{
                    Log.w("","WE MADE IT TO THE ELSE STATEMENT!")
                    val votes = p.calc_perc_n_lock()
                    if (numOfOptions == 6) {
                        v = LayoutInflater.from(context).inflate(R.layout.fragment_vote6_result, null)
                        layoutThingy = R.layout.fragment_vote6_result
                        val b6 = v.findViewById<TextView>(R.id.resultView6)
                        var ref = db.child("Public Polls").child(p.id.toString()).child("opt6Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b6.text = p.poll_options?.get(5).toString() + " = " +
                                       votes[5] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })

                        val b1 = v.findViewById<TextView>(R.id.resultView)
                        ref = db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b1.text = p.poll_options?.get(0).toString() + " = " +
                                        votes[0] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })

                        val b2 = v.findViewById<TextView>(R.id.resultView2)
                        ref = db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b2.text = p.poll_options?.get(1).toString() + " = " +
                                        votes[1] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        val b3 = v.findViewById<TextView>(R.id.resultView3)
                        ref = db.child("Public Polls").child(p.id.toString()).child("opt3Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b3.text = p.poll_options?.get(2).toString() + " = " +
                                        votes[2] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        val b4 = v.findViewById<TextView>(R.id.resultView4)
                        ref = db.child("Public Polls").child(p.id.toString()).child("opt4Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b4.text = p.poll_options?.get(3).toString() + " = " +
                                        votes[3] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        val b5 = v.findViewById<TextView>(R.id.resultView5)
                        ref = db.child("Public Polls").child(p.id.toString()).child("opt5Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b5.text = p.poll_options?.get(4).toString() + " = " +
                                        votes[4] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        var popupView =
                            LayoutInflater.from(getActivity(context)).inflate(layoutThingy, null);
                        var popupWindow = PopupWindow(
                            v,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT
                        );
                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                        v.setOnTouchListener { v, event ->
                            popupWindow.dismiss()
                            true
                        }

                        //
                    } else if (numOfOptions == 5) {
                        v = LayoutInflater.from(context).inflate(R.layout.fragment_vote5_result, null)
                        layoutThingy = R.layout.fragment_vote5_result
                        val b1 = v.findViewById<TextView>(R.id.resultView)
                        var ref = db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b1.text = p.poll_options?.get(0).toString() + " = " +
                                        votes[0] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })

                        val b2 = v.findViewById<TextView>(R.id.resultView2)
                        ref = db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b2.text = p.poll_options?.get(1).toString() + " = " +
                                        votes[1] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        val b3 = v.findViewById<TextView>(R.id.resultView3)
                        ref = db.child("Public Polls").child(p.id.toString()).child("opt3Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b3.text = p.poll_options?.get(3).toString() + " = " +
                                        votes[2] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        val b4 = v.findViewById<TextView>(R.id.resultView4)
                        ref = db.child("Public Polls").child(p.id.toString()).child("opt4Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b4.text = p.poll_options?.get(3).toString() + " = " +
                                        votes[3] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        val b5 = v.findViewById<TextView>(R.id.resultView5)
                        ref = db.child("Public Polls").child(p.id.toString()).child("opt5Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b5.text = p.poll_options?.get(4).toString() + " = " +
                                        votes[4] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        var popupView =
                            LayoutInflater.from(getActivity(context)).inflate(layoutThingy, null);
                        var popupWindow = PopupWindow(
                            v,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT
                        );
                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                        v.setOnTouchListener { v, event ->
                            popupWindow.dismiss()
                            true
                        }
                        //
                    } else if (numOfOptions == 4) {
                        v = LayoutInflater.from(context).inflate(R.layout.fragment_vote4_result, null)
                        layoutThingy = R.layout.fragment_vote4_result
                        val b1 = v.findViewById<TextView>(R.id.resultView)
                        var ref = db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b1.text = p.poll_options?.get(0).toString() + " = " +
                                        votes[0] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })

                        val b2 = v.findViewById<TextView>(R.id.resultView2)
                        ref = db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b2.text = p.poll_options?.get(1).toString() + " = " +
                                        votes[1] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        val b3 = v.findViewById<TextView>(R.id.resultView3)
                        ref = db.child("Public Polls").child(p.id.toString()).child("opt3Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b3.text = p.poll_options?.get(3).toString() + " = " +
                                        votes[2] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        val b4 = v.findViewById<TextView>(R.id.resultView4)
                        ref = db.child("Public Polls").child(p.id.toString()).child("opt4Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b4.text = p.poll_options?.get(3).toString() + " = " +
                                        votes[3] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        var popupView =
                            LayoutInflater.from(getActivity(context)).inflate(layoutThingy, null);
                        var popupWindow = PopupWindow(
                            v,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT
                        );
                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                        v.setOnTouchListener { v, event ->
                            popupWindow.dismiss()
                            true
                        }
                        //
                    } else if (numOfOptions == 3) {
                        v = LayoutInflater.from(context).inflate(R.layout.fragment_vote3_result, null)
                        layoutThingy = R.layout.fragment_vote3_result
                        val b1 = v.findViewById<TextView>(R.id.resultView)
                        var ref = db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b1.text = p.poll_options?.get(0).toString() + " = " +
                                        votes[0] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })

                        val b2 = v.findViewById<TextView>(R.id.resultView2)
                        ref = db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b2.text = p.poll_options?.get(1).toString() + " = " +
                                        votes[1] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        val b3 = v.findViewById<TextView>(R.id.resultView3)
                        ref = db.child("Public Polls").child(p.id.toString()).child("opt3Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b3.text = p.poll_options?.get(3).toString() + " = " +
                                        votes[2] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        var popupView =
                            LayoutInflater.from(getActivity(context)).inflate(layoutThingy, null);
                        var popupWindow = PopupWindow(
                            v,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT
                        );
                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                        v.setOnTouchListener { v, event ->
                            popupWindow.dismiss()
                            true
                        }
                        //
                    } else {
                        Log.w("","I guess we're here???")
                        v = LayoutInflater.from(context).inflate(R.layout.fragment_vote2_result, null)
                        layoutThingy = R.layout.fragment_vote2_result
                        val title = v.findViewById<Button>(R.id.vote_poll_title)
                        title.text = p.business_name
                        val b1 = v.findViewById<TextView>(R.id.resultView)
                        var ref = db.child("Public Polls").child(p.id.toString()).child("opt1Vote")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b1.text = p.poll_options?.get(0).toString() + " = " +
                                        votes[0] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        var ref2 = db.child("Public Polls").child(p.id.toString()).child("opt2Vote")
                        val b2 = v.findViewById<TextView>(R.id.resultView2)
                        ref2.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                b2.text = p.poll_options?.get(1).toString() + " = " +
                                        votes[1] + "%"
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.w("WARNING","")
                            }
                        })
                        var popupView =
                            LayoutInflater.from(getActivity(context)).inflate(layoutThingy, null);
                        var popupWindow = PopupWindow(
                            v,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT
                        );
                        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                        v.setOnTouchListener { v, event ->
                            popupWindow.dismiss()
                            true
                        }
                        //
                    }
                }

                //var popupView = LayoutInflater.from(getActivity(context)).inflate(layoutThingy, null);
                //var popupWindow = PopupWindow(v, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                //popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

//                val popupWindow = PopupWindow(itemView, width, height, focusable)
                // show the popup window
//                popupWindow.showAtLocation(temp, Gravity.CENTER, 0, 0)
            }
        }
    }
}
