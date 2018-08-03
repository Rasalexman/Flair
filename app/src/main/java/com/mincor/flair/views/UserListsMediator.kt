package com.mincor.flair.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import com.mincor.flair.R
import com.mincor.flairframework.core.animation.LinearAnimator
import com.mincor.flairframework.ext.log
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
    val mvpMediator: MVPMediator by mediatorLazy()

    override fun createLayout(context: Context): View = inflateView(R.layout.simple_layout) //UserListUI().createView(AnkoContext.create(context, this))

    private var button: Button? = null
    private var button2: Button? = null

    override fun onCreatedView(view: View) {
        toolBar = view.simple_tool_bar
        super.onCreatedView(view)

        button = view.button
        button!!.onClick {
            mvpMediator.show(animation = LinearAnimator())
        }

        button2 = view.button2
        button2!!.onClick { popTo<MVPMediator>() }
    }

    override fun onAddedView(view: View) {
        super.onAddedView(view)
        setHomeButtonEnable()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // clear the references, so there is no memory leaks )
        button = null
        button2 = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_new -> log { "-------> HELLO FROM action_new" }
                R.id.action_blacklist -> log{"-------> HELLO FROM action_blacklist"}
                R.id.app_bar_switch -> log{"-------> HELLO FROM app_bar_switch" }
            }
        return super.onOptionsItemSelected(item)
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