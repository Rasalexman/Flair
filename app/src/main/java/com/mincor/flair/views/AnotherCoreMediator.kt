package com.mincor.flair.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.mincor.flair.R
import com.mincor.flair.application.FlairApplication
import com.mincor.flair.views.subcomponents.SubChildCoreMediator
import com.mincor.flairframework.core.animation.LinearAnimator
import com.mincor.flairframework.interfaces.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk25.coroutines.onClick

class AnotherCoreMediator : ToolbarMediator() {

    private var anotherLayout:ViewGroup? = null

    override fun createLayout(context: Context): View = AnotherCoreUI().createView(AnkoContext.create(context, this))

    override fun onAddedView(view: View) {
        super.onAddedView(view)
        flair(FlairApplication.CORE_SECOND).attach(activity, anotherLayout!!).showLastOrExistMediator<SubChildCoreMediator>()
        setHomeButtonEnable()
    }

    override fun onRemovedView(view: View) {
        super.onRemovedView(view)
        flair(FlairApplication.CORE_SECOND).remove()
    }

    inner class AnotherCoreUI : AnkoComponent<AnotherCoreMediator> {
        override fun createView(ui: AnkoContext<AnotherCoreMediator>): View = with(ui) {
            verticalLayout {
                lparams(matchParent, matchParent)

                toolBar = toolbar {
                    setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                    title = "Another core mediator"
                    backgroundResource = R.color.colorPrimary
                }

                anotherLayout = relativeLayout {

                }.lparams(matchParent, matchParent) {
                    weight = 100f
                }

                button("POP TO PARENT MEDIATOR") {
                    onClick {
                        popTo<MVVMMediator>()
                    }
                }.lparams(matchParent) {
                    weight = 1f
                }
            }
        }
    }
}