package com.mincor.flair.views.pager

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import com.mincor.flair.activity.log
import com.mincor.flairframework.patterns.mediator.Mediator
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class PageOneMediator : Mediator() {

    override fun createLayout(context: Context): View = with(context) {
        verticalLayout {
            lparams(matchParent, matchParent)

            textView("THIS IS A PAGE ONE MEDIATOR") {
                textSize = 14f
                textColor = Color.GREEN
            }.lparams {
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }
    }

    override fun onCreatedView(view: View) {
        super.onCreatedView(view)
        log { "------> VIEW PAGE ONE CREATED" }
    }

    override fun onRemovedView() {
        super.onRemovedView()
        log { "------> VIEW PAGE ONE REMOVED" }
    }
}