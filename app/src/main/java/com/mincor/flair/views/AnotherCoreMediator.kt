package com.mincor.flair.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.mincor.flair.R
import com.mincor.flair.application.FlairApplication
import com.mincor.flair.views.subcomponents.SubChildCoreMediator
import com.rasalexman.flairframework.interfaces.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk27.coroutines.onClick

class AnotherCoreMediator : ToolbarMediator() {

    private var anotherLayout:ViewGroup? = null

    override var hashBackButton: Boolean = true

    override fun createLayout(context: Context): View = AnotherCoreUI().createView(AnkoContext.create(context, this))

    override fun onAddedView(view: View) {
        super.onAddedView(view)
        flair(FlairApplication.CORE_SECOND).attach(activity, anotherLayout!!).showLastOrExistMediator<SubChildCoreMediator>()
    }

    override fun onRemovedView(view: View) {
        super.onRemovedView(view)
        anotherLayout = null
        flair(FlairApplication.CORE_SECOND).remove()
    }

    override fun handleBackButton(animation: IAnimator?): Boolean {
        return if(!flair(FlairApplication.CORE_SECOND).handleBackButton(animation)) super.handleBackButton(animation) else true
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