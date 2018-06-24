package com.mincor.flair.views.subcomponents

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.mincor.flair.views.MVVMMediator
import com.mincor.flairframework.core.animation.LinearAnimator
import com.mincor.flairframework.interfaces.*
import com.mincor.flairframework.patterns.mediator.Mediator
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*

class NextSubChildMediator : Mediator() {

    private var childNameTV:TextView? = null

    override fun onCreateView(context: Context) {
        viewComponent = NextChildUI().createView(AnkoContext.create(context, this))
    }

    override fun onAddedView() {
        childNameTV?.text = this.mediatorName
    }

    fun onAnotherClickHandler() {
        facade.retrieveMediator<NextSubChildMediator>(UUID.randomUUID().toString()).show(LinearAnimator())
    }

    inner class NextChildUI : AnkoComponent<NextSubChildMediator> {
        override fun createView(ui: AnkoContext<NextSubChildMediator>): View = with(ui) {
            verticalLayout {
                lparams(matchParent, matchParent)

                childNameTV = textView {
                    textSize = 18f
                    textColor = Color.BLACK
                }.lparams(matchParent) {
                    margin = dip(16)
                }

                button("next child") {
                    onClick {
                        onAnotherClickHandler()
                    }
                }

                button("POP TO ROOT CHILD") {
                    onClick {
                        popTo<SubChildCoreMediator>()
                    }
                }

                button("POP TO PARENT MEDAITOR"){
                    onClick {
                        onShowParentMediatorHandler()
                    }
                }
            }
        }
    }

    private fun onShowParentMediatorHandler() {
        flair(IFacade.DEFAULT_KEY).retrieveMediator<MVVMMediator>().show(LinearAnimator())
    }
}