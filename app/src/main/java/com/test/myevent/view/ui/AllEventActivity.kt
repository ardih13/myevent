package com.test.myevent.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.myevent.R
import com.test.myevent.data.model.EventModel
import com.test.myevent.data.room.CallRoomEvent
import com.test.myevent.view.adapter.AllEventAdapter
import kotlinx.android.synthetic.main.activity_all_event.*
import kotlinx.android.synthetic.main.toolbar.*

class AllEventActivity : AppCompatActivity() {

    private val listAllEvent = ArrayList<EventModel>()
    private var allEventAdapter: AllEventAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_event)

        setupUI()
        getListAllEvent()
    }

    private fun setupUI() {
        tvTittleToolbar.text = getString(R.string.semua_event)

        ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getListAllEvent() {
        this.let { CallRoomEvent.getAllEvent(it) }.let {
            listAllEvent.clear()
            listAllEvent.addAll(it)
        }

        allEventAdapter = AllEventAdapter(this, listAllEvent)

        val currentLayoutManager = LinearLayoutManager(this)
        rvAllEvent?.apply {
            layoutManager = currentLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = allEventAdapter
        }
    }
}