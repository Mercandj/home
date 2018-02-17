package com.mercandalli.android.home.gpio

import android.content.ContentValues
import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mercandalli.android.home.R

class GpioCardView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private var gpio7TextView: TextView? = null
    private var gpio7SeekBar: SeekBar? = null

    private val valueEventListener = createValueEventListener()
    private val databaseReferenceGpio = FirebaseDatabase.getInstance().getReference("gpio")

    init {
        LayoutInflater.from(context).inflate(R.layout.view_gpio, this)
        gpio7TextView = findViewById(R.id.activity_main_gpio7_refresh_rate_textview)
        gpio7SeekBar = findViewById(R.id.activity_activity_gpio7_refresh_rate_seekbar)

        gpio7SeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                syncGpio7Text(seekBar!!.progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val ms = seekBar!!.progress
                databaseReferenceGpio.child("7").setValue(ms)
                syncGpio7Text(ms)
            }
        })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        databaseReferenceGpio.child("7").addValueEventListener(valueEventListener)
    }

    override fun onDetachedFromWindow() {
        databaseReferenceGpio.child("7").removeEventListener(valueEventListener)
        super.onDetachedFromWindow()
    }

    private fun syncGpio7Text(rate: Int) {
        gpio7TextView!!.text = "Gpio7 rate: " + rate + " ms"
    }

    private fun createValueEventListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val refreshRate = dataSnapshot.getValue<Int>(Int::class.java)!!
                syncGpio7Text(refreshRate)
                gpio7SeekBar!!.progress = refreshRate
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        }
    }

    fun setTitle(title: String) {

    }

}
