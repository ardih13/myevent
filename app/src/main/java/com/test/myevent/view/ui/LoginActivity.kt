package com.test.myevent.view.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.test.myevent.R
import com.test.myevent.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initPermission()
        setupUI()
    }


    private fun setupUI() {
        btnLogin.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }


}