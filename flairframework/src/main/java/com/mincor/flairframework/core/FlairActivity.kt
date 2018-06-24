package com.mincor.flairframework.core

import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.mincor.flairframework.interfaces.common.IActionBarProvider

/**
 * Main Activity class that support FlairFramework
 */
open class FlairActivity : AppCompatActivity(), IActionBarProvider<ActionBar, Toolbar> {
    override fun setSupportActionBar(toolbar: Toolbar?) {
        toolbar?.let {
            super.setSupportActionBar(it)
        }
    }
}