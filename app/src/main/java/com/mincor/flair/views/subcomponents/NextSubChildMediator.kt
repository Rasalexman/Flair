package com.mincor.flair.views.subcomponents

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.mincor.flair.proxies.NextSubProxy
import com.mincor.flair.views.MainMediator
import com.rasalexman.flaircore.animation.LinearAnimator
import com.rasalexman.flaircore.interfaces.*
import com.rasalexman.flaircore.patterns.mediator.Mediator
import com.rasalexman.flairreflect.proxy
import com.rasalexman.flairreflect.showMediator
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class NextSubChildMediator : Mediator(), NextSubProxy.IView {

    private var childNameTV:TextView? = null

    override fun createLayout(context: Context): View = NextChildUI().createView(AnkoContext.create(context, this))

    override fun onAddedView(view: View) {
        childNameTV?.text = this.mediatorName
        proxy<NextSubProxy>(this).callViewNameToMediator()
    }

    override fun onProxyCalledHandler(str: String?) {
        activity.toast("HELLOW FROM NEW PROXY AND MEDIATOR NAME $str").show()
    }

    fun onAnotherClickHandler() {
        showMediator<NextSubChildMediator>(LinearAnimator(), UUID.randomUUID().toString())
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
        flair(IFacade.DEFAULT_KEY).retrieveMediator<MainMediator>().show(LinearAnimator(), true)
    }
}