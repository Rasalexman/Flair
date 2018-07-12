package com.mincor.flair.views.subcomponents

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.mincor.flair.proxies.NextSubProxy
import com.mincor.flair.views.MVVMMediator
import com.mincor.flairframework.core.animation.LinearAnimator
import com.mincor.flairframework.interfaces.*
import com.mincor.flairframework.patterns.mediator.Mediator
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*

class NextSubChildMediator : Mediator(), NextSubProxy.IView {

    private var childNameTV:TextView? = null

    override fun createLayout(context: Context): View = NextChildUI().createView(AnkoContext.create(context, this))

    override fun onAddedView() {
        childNameTV?.text = this.mediatorName
        proxy<NextSubProxy>(this).callViewNameToMediator()
    }

    override fun onProxyCalledHandler(str: String?) {
        activity.toast("HELLOW FROM NEW PROXY AND MEDIATOR NAME $str").show()
    }

    fun onAnotherClickHandler() {
        showMediator<NextSubChildMediator>(UUID.randomUUID().toString(), LinearAnimator())
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

                button("previous child") {
                    onClick {
                        popToBack(LinearAnimator())
                    }
                }

                button("POP TO ROOT CHILD") {
                    onClick {
                        popTo<SubChildCoreMediator>(null, LinearAnimator())
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
        flair(IFacade.DEFAULT_KEY).retrieveMediator<MVVMMediator>().show(LinearAnimator(), true)
    }
}