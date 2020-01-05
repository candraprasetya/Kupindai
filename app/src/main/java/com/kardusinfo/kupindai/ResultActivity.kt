/*
 * Copyright (c) 2019 Kardusinfo. All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Candra R Prasetya <candraramadhanp@outlook.com>, 11 2019
 */

package com.kardusinfo.kupindai

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long=3000 // 3 sec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        foodNameText.text = intent.getStringExtra("TITLE")

        var resultDialog = Dialog(this)
        val customWaitingView = LayoutInflater.from(this).inflate(R.layout.waiting_dialog, null)
        resultDialog.setCancelable(false)
        resultDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        resultDialog.setContentView(customWaitingView)

        kirimData.setOnClickListener {
            resultDialog.show()
            Handler().postDelayed({
               resultDialog.cancel()
                Toast.makeText(this, "Terimakasih Telah Mengajariku", Toast.LENGTH_SHORT).show()
                finish()
            }, SPLASH_TIME_OUT)

        }

    }


}
