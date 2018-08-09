package com.mincor.flair.views.subcomponents

import android.content.Context
import android.view.View
import com.rasalexman.flairframework.core.animation.LinearAnimator
import com.rasalexman.flairframework.patterns.mediator.Mediator
import com.rasalexman.flairframework.interfaces.popTo
import com.rasalexman.flairframework.interfaces.showMediator
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class SubChildCoreMediator : Mediator() {

    override fun createLayout(context: Context): View = SubChildUI().createView(AnkoContext.create(context, this))

    fun onAnotherClickHandler() {
        showMediator<NextSubChildMediator>(null, LinearAnimator())
    }

    inner class SubChildUI : AnkoComponent<SubChildCoreMediator> {
        override fun createView(ui: AnkoContext<SubChildCoreMediator>): View = with(ui) {
            verticalLayout {
                lparams(matchParent, matchParent)

                button("another child") {
                    onClick {
                        onAnotherClickHandler()
                    }
                }

                button("POP TO ROOT CHILD") {
                    onClick {
                        popTo<SubChildCoreMediator>()
                    }
                }
            }
        }
    }
}