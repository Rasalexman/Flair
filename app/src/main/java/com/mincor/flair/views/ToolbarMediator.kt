package com.mincor.flair.views

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import com.rasalexman.flaircore.animation.LinearAnimator
import com.rasalexman.flaircore.interfaces.activity
import com.rasalexman.flaircore.interfaces.popToBack
import com.rasalexman.flaircore.patterns.mediator.Mediator

/**
 * Created by a.minkin.
 */
abstract class ToolbarMediator : Mediator(), View.OnClickListener {

    companion object {
        const val BACK_BUTTON_ID = -1
    }

    protected open var hashBackButton = false

    protected var toolBar: Toolbar? = null

    override fun onAddedView(view: View) {
        super.onAddedView(view)
        toolBar?.let {
            setActionBar(it)
            setHomeButtonEnable()
        }
    }

    override fun onRemovedView(view: View) {
        super.onRemovedView(view)
        toolBar?.setNavigationOnClickListener(null)
    }

    override fun onDestroyView() {
        toolBar = null
        super.onDestroyView()
    }

    private val actionBar: ActionBar?
        get() = (activity as? AppCompatActivity)?.supportActionBar

    private fun setActionBar(toolbar: Toolbar?) {
        toolbar?.let {
            (activity as? AppCompatActivity)?.setSupportActionBar(it)
            it.setNavigationOnClickListener(this)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            BACK_BUTTON_ID -> popToBack(LinearAnimator())
        }
    }

    private fun setHomeButtonEnable() {
        //set the back arrow in the toolbar
        actionBar?.setDisplayHomeAsUpEnabled(hashBackButton)
    }
}