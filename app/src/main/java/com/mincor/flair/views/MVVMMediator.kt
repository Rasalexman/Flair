package com.mincor.flair.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.mincor.flair.R
import com.mincor.flair.activity.log
import com.mincor.flair.events.Events
import com.mincor.flair.proxies.MVVMProxy
import com.mincor.flair.proxies.UserProxy
import com.mincor.flair.proxies.vo.AccountModel
import com.mincor.flair.proxies.vo.UserModel
import com.mincor.flair.utils.Keyboards
import com.mincor.flairframework.core.animation.LinearAnimator
import com.mincor.flairframework.interfaces.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onEditorAction
import org.jetbrains.anko.sdk25.coroutines.textChangedListener
import java.util.*

/**
 * Created by a.minkin.
 */
class MVVMMediator : ToolbarMediator() {

    override var hasOptionalMenu: Boolean = true

    var name = ""
    var password = ""

    val accountModel: AccountModel by proxyLazyModel<MVVMProxy, AccountModel>()

    val usersList:MutableList<UserModel> by proxyLazyModel<UserProxy, MutableList<UserModel>>()

    // you can use it like a lazy reference
    val accountListMediator: UserListsMediator by mediatorLazy()

    var accountNameTV:TextView? = null
    var passwordNameTV:TextView? = null

    override fun onRegister() {
        registerObserver(UserProxy.NOTIFICATION_AUTH_COMPLETE) {
            println("------> NOTIFICATION AUTH COMPLETE, SIZE = ${usersList.size}")
            accountListMediator.show(LinearAnimator())
        }.registerObserver( MVVMProxy.ACCOUNT_CHANGE_HANLDER) {
            println("------> ACCOUNT PROXY social name = ${accountModel.socialName} pageId = ${accountModel.pageId}")

            accountNameTV?.text = accountModel.socialName
            passwordNameTV?.text = accountModel.pageId

        }.registerListObservers(arrayListOf(UserProxy.NOTIFICATION_AUTH_FAILED, UserProxy.NOTIFICATION_AUTH_COMPLETE)) {
            Toast.makeText(activity, "Login Failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun createLayout(context: Context): View = UserAuthUI().createView(AnkoContext.create(context, this))

    fun nameUpdated(newName: String) {
        name = newName
    }

    private fun passwordUpdated(newPassword: String) {
        password = newPassword
    }

    private fun handleEditorAction(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            onLoginClicked()
            return true
        }
        return false
    }

    private fun onLoginClicked() {
        Keyboards.hideKeyboard(viewComponent!!.context, viewComponent!!)
        println("-----> account first data $accountModel")
        sendNotification(Events.AUTH, arrayOf(name, password))
    }

    private fun onHideClicked() {
        sendNotification(Events.ACCOUNT_CHANGE, arrayListOf("${UUID.randomUUID()}", "id${Math.random()*100_000L.toInt()}"))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_auth, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> log { "SETTINGS CLICKED" }
            R.id.action_about -> log { "ABOUT CLICKED" }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class UserAuthUI : AnkoComponent<MVVMMediator> {
        override fun createView(ui: AnkoContext<MVVMMediator>) = with(ui) {
            verticalLayout {
                lparams(matchParent, matchParent)
                toolBar = toolbar {
                    setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                    title = "Login"
                    backgroundResource = R.color.colorPrimary
                }

                accountNameTV = editText(name) {
                    hint = "Username"
                    imeOptions = EditorInfo.IME_ACTION_NEXT
                    singleLine = true
                    textChangedListener {
                        onTextChanged { text, _, _, _ ->
                            nameUpdated(text.toString())
                        }
                    }
                }

                passwordNameTV = editText(password) {
                    hint = "Password"
                    singleLine = true
                    imeOptions = EditorInfo.IME_ACTION_SEND
                    inputType = InputType.TYPE_CLASS_TEXT
                    textChangedListener {
                        onTextChanged { text, _, _, _ ->
                            passwordUpdated(text.toString())
                        }
                    }
                    onEditorAction { _, actionId, _ -> handleEditorAction(actionId) }
                }

                button("Login") {
                    onClick { onLoginClicked() }
                }

                button("GENERATE LIVE DATA") {
                    onClick { onHideClicked() }
                }

                button("SHOW SUB CHILD MEDIATOR") {
                    onClick {
                        facade.retrieveMediator<AnotherCoreMediator>().show()
                    }
                }
            }
        }
    }
}


