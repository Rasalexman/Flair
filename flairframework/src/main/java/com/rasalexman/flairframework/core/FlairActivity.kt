package com.rasalexman.flairframework.core

import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.rasalexman.flairframework.interfaces.common.IActionBarProvider

/**
 * Main Activity class that support FlairFramework
 */
open class FlairActivity : AppCompatActivity(), IActionBarProvider<ActionBar, Toolbar> {
    /**
     * Set the support action bar if needed
     */
    override fun setSupportActionBar(toolbar: Toolbar?) {
        toolbar?.let {
            super.setSupportActionBar(it)
        }
    }
}