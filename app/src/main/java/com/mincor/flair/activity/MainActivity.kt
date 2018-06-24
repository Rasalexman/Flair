package com.mincor.flair.activity

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import com.mincor.flair.BuildConfig
import com.mincor.flair.views.MVVMMediator
import com.mincor.flairframework.core.animation.LinearAnimator
import com.mincor.flairframework.ext.flair
import com.mincor.flairframework.interfaces.common.IActionBarProvider
import com.mincor.flairframework.interfaces.showLastOrExistMediator

class MainActivity : AppCompatActivity(), IActionBarProvider<ActionBar, Toolbar> {

    override fun setSupportActionBar(toolbar: Toolbar?) {
        toolbar?.let {
            super.setSupportActionBar(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flair().attach(this).showLastOrExistMediator<MVVMMediator>(LinearAnimator())
    }
}

inline fun log(lambda: () -> String) {
    if (BuildConfig.DEBUG) {
        Log.d("KOTLIN_TAG", lambda())
    }
}
