package com.mincor.flair.views

import android.content.Context
import androidx.core.content.ContextCompat
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import com.mincor.flair.R
import com.rasalexman.flaircore.animation.LinearAnimator
import com.rasalexman.flaircore.ext.log
import com.rasalexman.flaircore.interfaces.*
import com.rasalexman.flairreflect.mediatorLazy
import kotlinx.android.synthetic.main.simple_layout.view.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by a.minkin.
 */
class UserListsMediator : ToolbarMediator() {

    override var hasOptionalMenu: Boolean = true
    override var hashBackButton: Boolean = true

    val mvpMediator: MVPMediator by mediatorLazy()

    // You can use any time of layout (XML, Code or Anko DSL)
    override fun createLayout(context: Context): View = inflateView(R.layout.simple_layout) //UserListUI().createView(AnkoContext.create(context, this))

    private var button: Button? = null
    private var button2: Button? = null

    override fun onCreatedView(view: View) {
        toolBar = view.simple_tool_bar
        super.onCreatedView(view)

        button = view.button
        button?.apply {
            // You can manage your xml synthetic layout by included drawables with round corners
            background = roundedBg(color(R.color.colorPurpleLight))

            onClick {
                mvpMediator.show(animation = LinearAnimator())
            }
        }

        button2 = view.button2
        button2?.onClick { popTo<MVPMediator>() }
    }

    override fun onAddedView(view: View) {
        super.onAddedView(view)
        // take parameters across all mediators
        view.login.text = arguments.getString(MainMediator.BUNDLE_NAME, "")
        view.password.text = arguments.getString(MainMediator.BUNDLE_PASSWORD, "")
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

    // USE inner classes to easy link with ui variables or use `ui.owner` member property (with static classes)
    class UserListUI : AnkoComponent<UserListsMediator> {
        override fun createView(ui: AnkoContext<UserListsMediator>): View = with(ui) {
            verticalLayout {
                lparams(matchParent, matchParent)

                ui.owner.toolBar = toolbar {
                    setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                    title = "User List"
                    backgroundResource = R.color.colorPrimary
                }

                ui.owner.button = button("show mvp") {
                    onClick {
                        ui.owner.mvpMediator.show(LinearAnimator())
                    }
                }

                button("POP TO NOT REGISTERED MEDIATOR") {
                    onClick {
                        ui.owner.popTo<MainMediator>()
                    }
                }
            }
        }
    }
}