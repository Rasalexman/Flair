package com.mincor.flair.views.subcomponents

import android.content.Context
import android.view.View
import com.rasalexman.flaircore.animation.LinearAnimator
import com.rasalexman.flaircore.interfaces.popTo
import com.rasalexman.flaircore.patterns.mediator.Mediator
import com.rasalexman.flairreflect.showMediator
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class SubChildCoreMediator : Mediator() {

    override fun createLayout(context: Context): View = SubChildUI().createView(AnkoContext.create(context, this))

    fun onAnotherClickHandler() {
        showMediator<NextSubChildMediator>(LinearAnimator())
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