package com.mincor.flair.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.mincor.flair.R
import com.mincor.flair.application.FlairApplication
import com.mincor.flair.views.subcomponents.SubChildCoreMediator
import com.rasalexman.flaircore.common.bundle.bundleValue
import com.rasalexman.flaircore.interfaces.*
import com.rasalexman.flairreflect.showLastOrExistMediator
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk27.coroutines.onClick

class AnotherCoreMediator : ToolbarMediator() {

    private var anotherLayout: ViewGroup? = null

    override var hashBackButton: Boolean by bundleValue(true)

    override fun createLayout(context: Context): View = AnotherCoreUI().createView(AnkoContext.create(context, this))

    override fun onAddedView(view: View) {
        super.onAddedView(view)
        val currentActivity = activity
        anotherLayout?.let { layout ->
            flair(FlairApplication.CORE_SECOND) {
                attach(currentActivity, layout)
                showLastOrExistMediator<SubChildCoreMediator>()
            }
        }
    }

    override fun onRemovedView(view: View) {
        super.onRemovedView(view)
        anotherLayout = null
        flair(FlairApplication.CORE_SECOND).remove()
    }

    override fun handleBackButton(animation: IAnimator?): Boolean {
        return if (!flair(FlairApplication.CORE_SECOND).handleBackButton(animation)) super.handleBackButton(animation) else true
    }

    class AnotherCoreUI : AnkoComponent<AnotherCoreMediator> {
        override fun createView(ui: AnkoContext<AnotherCoreMediator>): View = with(ui) {
            verticalLayout {
                lparams(matchParent, matchParent)

                ui.owner.toolBar = toolbar {
                    setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                    title = "Another core mediator"
                    backgroundResource = R.color.colorPrimary
                }

                ui.owner.anotherLayout = relativeLayout {

                }.lparams(matchParent, matchParent) {
                    weight = 100f
                }

                button("POP TO PARENT MEDIATOR") {
                    onClick {
                        ui.owner.popTo<MainMediator>()
                    }
                }.lparams(matchParent) {
                    weight = 1f
                }
            }
        }
    }
}