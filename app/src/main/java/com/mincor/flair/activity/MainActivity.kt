package com.mincor.flair.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mincor.flair.views.MainMediator
import com.rasalexman.flaircore.ext.flair
import com.rasalexman.flaircore.interfaces.attach
import com.rasalexman.flaircore.interfaces.handleBackButton
import com.rasalexman.flairreflect.showLastOrExistMediator

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
