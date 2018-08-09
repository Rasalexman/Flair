package com.mincor.flair.views.pager

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import com.rasalexman.flairframework.ext.log
import com.rasalexman.flairframework.patterns.mediator.Mediator
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class PageTwoMediator : Mediator() {

    override fun createLayout(context: Context): View = with(context) {
        verticalLayout {
            lparams(matchParent, matchParent)
            gravity = Gravity.CENTER_HORIZONTAL
            textView("THIS IS A PAGE TWO MEDIATOR") {
                textSize = 14f
                textColor = Color.RED
            }.lparams {
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }
    }

    override fun onCreatedView(view: View) {
        super.onCreatedView(view)
        log { "------> VIEW PAGE TWO CREATED" }
    }

    override fun onRemovedView(view: View) {
        super.onRemovedView(view)
        log { "------> VIEW PAGE TWO REMOVED" }
    }


}