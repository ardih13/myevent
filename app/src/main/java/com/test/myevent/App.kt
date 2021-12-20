package com.test.myevent

import android.app.Application
import android.R

import io.github.inflationx.calligraphy3.CalligraphyConfig

import io.github.inflationx.calligraphy3.CalligraphyInterceptor

import io.github.inflationx.viewpump.ViewPump


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ViewPump.init(
            ViewPump.builder().addInterceptor(
                CalligraphyInterceptor(
                    CalligraphyConfig.Builder().setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                        .setFontAttrId(R.attr.font).build()
                )
            ).build()
        )
    }
}