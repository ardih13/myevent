package com.test.myevent.view.dialog

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.test.myevent.R
import kotlinx.android.synthetic.main.dialog_create_event_success.*

class CreateEventSuccessDialog(
    @NonNull context: Context,
    style: Int,
    private val callback: ICreateEvent
) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_create_event_success)
        setCancelable(false)

        val lWindowParams: WindowManager.LayoutParams = WindowManager.LayoutParams()
        lWindowParams.copyFrom(window?.attributes)
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT
        lWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT
        this.window?.attributes = lWindowParams

        btnOk.setOnClickListener {
            this.dismiss()
            callback.onSuccessCreateEvent()
        }
    }

    override fun onBackPressed() {
        this.dismiss()
        callback.onSuccessCreateEvent()
        super.onBackPressed()
    }

    interface ICreateEvent {
        fun onSuccessCreateEvent()
    }
}