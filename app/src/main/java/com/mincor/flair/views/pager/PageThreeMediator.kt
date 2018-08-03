package com.mincor.flair.views.pager

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import com.mincor.flairframework.ext.log
import com.mincor.flairframework.patterns.mediator.Mediator
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class PageThreeMediator : Mediator() {

    override fun createLayout(context: Context): View = with(context) {
        verticalLayout {
            lparams(matchParent, matchParent)
            gravity = Gravity.CENTER_HORIZONTAL
            textView("THIS IS A PAGE THREE MEDIATOR") {
                textSize = 14f
                textColor = Color.BLUE
            }.lparams {
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }
    }

    override fun onCreatedView(view: View) {
        super.onCreatedView(view)
        log { "------> VIEW PAGE THREE CREATED" }
    }

    override fun onRemovedView(view: View) {
        super.onRemovedView(view)
        log { "------> VIEW PAGE THREE REMOVED" }
    }
}