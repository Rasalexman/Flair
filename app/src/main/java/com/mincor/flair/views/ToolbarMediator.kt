package com.mincor.flair.views

import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.View
import com.mincor.flairframework.core.animation.LinearAnimator
import com.mincor.flairframework.interfaces.activity
import com.mincor.flairframework.interfaces.common.IActionBarProvider
import com.mincor.flairframework.interfaces.popToBack
import com.mincor.flairframework.patterns.mediator.Mediator

/**
 * Created by a.minkin.
 */
abstract class ToolbarMediator : Mediator(), View.OnClickListener {

    companion object {
        const val BACK_BUTTON_ID = -1
    }

    protected var toolBar: Toolbar? = null

    override fun onCreatedView(context: View) {
        toolBar?.let {
            setActionBar(it)
        }
    }

    override fun onRemove() {
        super.onRemove()
        toolBar?.setNavigationOnClickListener(null)
        (activity as? IActionBarProvider<ActionBar, Toolbar>)?.setSupportActionBar(null)
        toolBar = null
    }

    private val actionBar: ActionBar?
        get() {
            val actionBarProvider = (activity as? IActionBarProvider<ActionBar, Toolbar>)
            return actionBarProvider?.getSupportActionBar()
        }

    protected fun setActionBar(toolbar: Toolbar?) {
        toolbar?.let {
            (activity as? IActionBarProvider<ActionBar, Toolbar>)?.setSupportActionBar(it)
            it.setNavigationOnClickListener(this)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            BACK_BUTTON_ID -> popToBack(LinearAnimator())
        }
    }

    protected fun setHomeButtonEnable() {
        //set the back arrow in the toolbar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}