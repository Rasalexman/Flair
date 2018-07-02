package com.mincor.flair.views.subcomponents

import android.content.Context
import android.view.View
import com.mincor.flairframework.core.animation.LinearAnimator
import com.mincor.flairframework.interfaces.flair
import com.mincor.flairframework.interfaces.popTo
import com.mincor.flairframework.interfaces.retrieveMediator
import com.mincor.flairframework.interfaces.show
import com.mincor.flairframework.patterns.mediator.Mediator
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class SubChildCoreMediator : Mediator() {

    override fun createLayout(context: Context): View = SubChildUI().createView(AnkoContext.create(context, this))

    fun onAnotherClickHandler() {
        flair().retrieveMediator<NextSubChildMediator>().show(LinearAnimator())
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