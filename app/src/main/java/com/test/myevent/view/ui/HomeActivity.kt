package com.test.myevent.view.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.test.myevent.R
import com.test.myevent.data.model.EventModel
import com.test.myevent.data.model.HomeMenuModel
import com.test.myevent.data.room.CallRoomEvent
import com.test.myevent.interfaces.ActionClick
import com.test.myevent.util.DateUtil
import com.test.myevent.view.adapter.EventAdapter
import com.test.myevent.view.adapter.HomeMenuAdapter
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.partial_home.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeActivity : AppCompatActivity() {

    private val listAllEvent = ArrayList<EventModel>()
    private val listTodayEvent = ArrayList<EventModel>()

    private val mItemMenu = mutableListOf<HomeMenuModel>()
    private var menuAdapter: HomeMenuAdapter? = null

    private var eventAdapter: EventAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupUI()
        setupMenuHome()
    }

    override fun onResume() {
        super.onResume()
        getTodayEvent()
        getListEvent()
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        tvGreeting.text = "Halo, Selamat ${DateUtil.getTimeText()}"

        tvAllEvent.setOnClickListener {
            startActivity(Intent(this@HomeActivity, AllEventActivity::class.java))
        }
    }

    private fun setupMenuHome() {

        mItemMenu.addAll(
            mutableListOf(
                HomeMenuModel(
                    R.id.menu_create_event,
                    R.drawable.ic_create_event,
                    getString(R.string.menu_create_event)
                ),
                HomeMenuModel(
                    R.id.menu_my_event,
                    R.drawable.ic_my_event,
                    getString(R.string.menu_my_event)
                ),
                HomeMenuModel(
                    R.id.menu_daftar_karyawan,
                    R.drawable.ic_default,
                    getString(R.string.menu_daftar_karyawan)
                ),
                HomeMenuModel(
                    R.id.menu_reservasi_meeting,
                    R.drawable.ic_default,
                    getString(R.string.menu_reservasi_meeting)
                ),
                HomeMenuModel(
                    R.id.menu_pengajuan_cuti,
                    R.drawable.ic_default,
                    getString(R.string.menu_pengajuan_cuti)
                ),
                HomeMenuModel(
                    R.id.menu_absensi,
                    R.drawable.ic_default,
                    getString(R.string.menu_absensi)
                ),
            )
        )

        menuAdapter = HomeMenuAdapter(this, mItemMenu, object : ActionClick {
            override fun clickItem(position: Int, value: Any) {
                val item = value as HomeMenuModel

                when (item.id) {
                    R.id.menu_create_event -> {
                        startActivity(Intent(this@HomeActivity, CreateEventActivity::class.java))
                    }
                    R.id.menu_my_event -> {
                        startActivity(Intent(this@HomeActivity, AllEventActivity::class.java))
                    }
                }
            }
        })

        rvMenu?.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 4)
            itemAnimator = DefaultItemAnimator()
            adapter = menuAdapter
        }
    }

    private fun getTodayEvent() {

        val dateNow = getDateNow()
        this.let { CallRoomEvent.getTodayEvent(this, dateNow) }.let {
            listTodayEvent.clear()
            listTodayEvent.addAll(it)
        }

        if (listTodayEvent.isEmpty()) {
            viewEventToday.visibility = View.INVISIBLE
        } else {
            viewEventToday.visibility = View.VISIBLE

            Glide.with(this)
                .load(listTodayEvent[0].image)
                .centerCrop()
                .placeholder(R.drawable.background_home)
                .into(ivEvent)
            tvTitleEvent.text = listTodayEvent[0].name
            tvRoomEvent.text = listTodayEvent[0].location
            tvMonthEvent.text = DateUtil.simplifyDateFormat(
                listTodayEvent[0].start_date,
                "yyyy-MM-dd",
                "MMM"
            )
            tvDateEvent.text = DateUtil.simplifyDateFormat(
                listTodayEvent[0].start_date,
                "yyyy-MM-dd",
                "dd"
            )
        }
    }


    private fun getListEvent() {
        this.let { CallRoomEvent.getAllEvent(it) }.let {
            listAllEvent.clear()
            listAllEvent.addAll(it)
        }

        if (listAllEvent.isEmpty()) {
            viewNewEvent.visibility = View.GONE
        } else {
            viewNewEvent.visibility = View.VISIBLE
        }

        eventAdapter = EventAdapter(this, listAllEvent)
        val currentLayoutManager = LinearLayoutManager(this)
        rvEvent?.apply {
            layoutManager = currentLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = eventAdapter
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateNow(): String {
        val dateFormatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        dateFormatter.isLenient = false
        val today = Date()
        return dateFormatter.format(today)
    }

}