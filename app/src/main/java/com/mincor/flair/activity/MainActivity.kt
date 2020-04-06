package com.mincor.flair.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mincor.flair.views.MainMediator
import com.rasalexman.flaircore.ext.attach
import com.rasalexman.flaircore.ext.flair
import com.rasalexman.flaircore.ext.handleBackButton
import com.rasalexman.flaircore.ext.showLastOrExistMediator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flair {
            attach(this@MainActivity)
            showLastOrExistMediator<MainMediator>()
        }
    }

    override fun onBackPressed() {
        if (!flair().handleBackButton()) {
            super.onBackPressed()
        }
    }
}
