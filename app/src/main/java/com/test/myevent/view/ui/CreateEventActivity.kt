package com.test.myevent.view.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.bumptech.glide.Glide
import com.test.myevent.IConfig
import com.test.myevent.R
import com.test.myevent.base.BaseActivity
import com.test.myevent.data.model.EventModel
import com.test.myevent.data.room.CallRoomEvent
import com.test.myevent.util.Base64ImageUtil
import com.test.myevent.util.DateUtil
import com.test.myevent.view.dialog.CreateEventSuccessDialog
import kotlinx.android.synthetic.main.activity_create_event.*
import kotlinx.android.synthetic.main.partial_create_event.*
import kotlinx.android.synthetic.main.partial_description_event.*
import kotlinx.android.synthetic.main.partial_image_event.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
import android.widget.TimePicker

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener


class CreateEventActivity : BaseActivity(), DatePickerDialog.OnDateSetListener,
    CreateEventSuccessDialog.ICreateEvent {

    var startDateEvent = ""
    var endDateEvent = ""

    var isStartDateEvent = false
    var startDateEventDialog: DatePickerDialog? = null

    var isEndDateEvent = false
    var endDateEventDialog: DatePickerDialog? = null

    private var fileImageBase64 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        initPermission()
        initDatePicker()
        setupUI()
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        tvTittleToolbar.text = getString(R.string.buat_event)

        ivBack.setOnClickListener {
            onBackPressed()
        }

        etStartDateEvent.setOnClickListener {
            isStartDateEvent = true
            isEndDateEvent = false
            startDateEventDialog?.show()
        }

        etEndDateEvent.setOnClickListener {
            isStartDateEvent = false
            isEndDateEvent = true
            endDateEventDialog?.show()
        }

        etStartTimeEvent.setOnClickListener {
            val c = Calendar.getInstance()
            val mHour = c[Calendar.HOUR_OF_DAY]
            val mMinute = c[Calendar.MINUTE]

            val startTimeEvent = TimePickerDialog(
                this, { _, hourOfDay, minute ->
                    etStartTimeEvent.setText("$hourOfDay:$minute")
                },
                mHour,
                mMinute,
                false
            )
            startTimeEvent.show()
        }

        etEndTimeEvent.setOnClickListener {
            val c = Calendar.getInstance()
            val mHour = c[Calendar.HOUR_OF_DAY]
            val mMinute = c[Calendar.MINUTE]

            val endTimeEvent = TimePickerDialog(
                this, { _, hourOfDay, minute ->
                    etEndTimeEvent.setText("$hourOfDay:$minute")
                },
                mHour,
                mMinute,
                false
            )
            endTimeEvent.show()
        }

        ivEvent.setOnClickListener {
            selectImage(this)
        }

        btnSave.setOnClickListener {
            createEvent()
            val dialog = CreateEventSuccessDialog(this, R.style.AppBottomSheetDialogTheme, this)
            dialog.show()
        }

    }

    private fun initDatePicker() {
        val myCalendar = Calendar.getInstance()

        startDateEventDialog = DatePickerDialog(
            this,
            this,
            myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        )

        endDateEventDialog = DatePickerDialog(
            this,
            this,
            myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var finalDateString = "$dayOfMonth"

        if (dayOfMonth < 10) {
            finalDateString = "0$dayOfMonth"
        }

        val finalMonth = month + 1
        var finalMonthString = "$finalMonth"

        if (finalMonth < 10) {
            finalMonthString = "0$finalMonth"
        }

        when {
            isStartDateEvent -> {
                startDateEvent = "$year-$finalMonthString-$finalDateString"
                etStartDateEvent.setText(
                    DateUtil.simplifyDateFormat(
                        startDateEvent,
                        "yyyy-MM-dd",
                        "dd MMM yyyy"
                    )
                )
            }
            isEndDateEvent -> {
                endDateEvent = "$year-$finalMonthString-$finalDateString"
                etEndDateEvent.setText(
                    DateUtil.simplifyDateFormat(
                        endDateEvent,
                        "yyyy-MM-dd",
                        "dd MMM yyyy"
                    )
                )
            }
        }
    }


    private fun createEvent() {
        val insert = EventModel()
        insert.name = etNameEvent.text.toString()
        insert.location = etLocationEvent.text.toString()
        insert.start_date = startDateEvent
        insert.end_date = endDateEvent
        insert.start_time = etStartTimeEvent.text.toString()
        insert.end_time = etEndTimeEvent.text.toString()
        insert.image = fileImageBase64
        insert.description = etDescriptionEvent.text.toString()
        insert.organizer = etOrganizer.text.toString()

        CallRoomEvent.insertEvent(applicationContext, insert)
    }


    override fun onSuccessCreateEvent() {
        onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            IConfig.REQUEST_GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let {
                        Base64ImageUtil.getPath(this, it)?.let { filePath ->
                            val file = File(filePath)

                            if (!file.exists()) {
                                Toast.makeText(
                                    this,
                                    "Gambar tidak ditemukan!\n${file.absolutePath}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                fileImageBase64 = Base64ImageUtil.fileImageToBase64(file)

                                Glide.with(this)
                                    .load(fileImageBase64)
                                    .centerCrop()
                                    .into(ivEvent)
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.gagal_mendapatkan_gambar, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            IConfig.REQUEST_CAMERA -> {
                if (resultCode == Activity.RESULT_OK) {
                    when {
                        data == null || data.extras == null || data.extras!!.get("data") == null -> {
                            Toast.makeText(
                                this,
                                R.string.gagal_mendapatkan_gambar,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            val bitmapImage = data.extras!!.get("data") as Bitmap

                            val folderMyEvent = File(getExternalFilesDir(null), IConfig.FOLDER_APP)
                            if (!folderMyEvent.exists())
                                folderMyEvent.mkdir()

                            val filename = "${IConfig.FOLDER_APP}/camera.jpeg"
                            val dest = File(getExternalFilesDir(null), filename)

                            try {
                                val out = FileOutputStream(dest)
                                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, out)
                                out.flush()
                                out.close()
                            } catch (e: Exception) {
                                Toast.makeText(this, "Exception", Toast.LENGTH_SHORT).show()
                            }

                            val file = File(getExternalFilesDir(null), filename)
                            if (!file.exists()) {
                                Toast.makeText(
                                    this,
                                    "Gambar tidak ditemukan!\n${file.absolutePath}",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } else {
                                fileImageBase64 = Base64ImageUtil.fileImageToBase64(file)

                                Glide.with(this)
                                    .load(fileImageBase64)
                                    .centerCrop()
                                    .into(ivEvent)
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.gagal_mendapatkan_gambar, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}