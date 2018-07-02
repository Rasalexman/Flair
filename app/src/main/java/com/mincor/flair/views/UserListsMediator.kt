package com.mincor.flair.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.mincor.flair.R
import com.mincor.flairframework.core.animation.LinearAnimator
import com.mincor.flairframework.interfaces.*
import kotlinx.android.synthetic.main.simple_layout.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by a.minkin.
 */
class UserListsMediator : ToolbarMediator() {

    override var hasOptionalMenu: Boolean = true

    val mvpMediator: MVPMediator by mediator()

    override fun createLayout(context: Context): View = inflateView(R.layout.simple_layout) //UserListUI().createView(AnkoContext.create(context, this))

    override fun onCreatedView(context: View) {
        toolBar = viewComponent?.simple_tool_bar
        super.onCreatedView(context)
        setHomeButtonEnable()

        val button = viewComponent!!.button
        button.onClick { mvpMediator.show(animation = LinearAnimator()) }

        val button2 = viewComponent!!.button2
        button2.onClick { popTo<MVPMediator>() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list, menu)
    }

    inner class UserListUI : AnkoComponent<UserListsMediator> {
        override fun createView(ui: AnkoContext<UserListsMediator>): View = with(ui) {
            verticalLayout {
                lparams(matchParent, matchParent)

                toolBar = toolbar {
                    setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                    title = "User List"
                    backgroundResource = R.color.colorPrimary
                }

                button("show mvp") {
                    onClick {
                        mvpMediator.show(LinearAnimator())
                    }
                }

                button("POP TO NOT REGISTERED MEDIATOR") {
                    onClick {
                        popTo<MVVMMediator>()
                    }
                }
            }
        }
    }
}