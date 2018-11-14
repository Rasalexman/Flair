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
import com.mincor.askme.mediators.login.LoginMediator
import com.mincor.flair.R
import com.mincor.flair.events.Events
import com.mincor.flair.proxies.MVVMProxy
import com.mincor.flair.proxies.UserProxy
import com.mincor.flair.proxies.rand
import com.mincor.flair.proxies.vo.AccountModel
import com.mincor.flair.proxies.vo.UserModel
import com.mincor.flair.utils.Keyboards
import com.mincor.flair.utils.dip16
import com.rasalexman.flairframework.core.animation.LinearAnimator
import com.rasalexman.flairframework.ext.log
import com.rasalexman.flairframework.interfaces.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

/**
 * Created by a.minkin.
 */
class MVVMMediator : ToolbarMediator() {

    companion object {
        const val BUNDLE_NAME = "user_name"
        const val BUNDLE_PASSWORD = "user_password"
    }

    override var hasOptionalMenu: Boolean = true

    // model as AccountModel
    private val accountModel by proxyLazyModel<MVVMProxy, AccountModel>()
    // models as List
    private val usersList by proxyLazyModel<UserProxy, MutableList<UserModel>>()
    // you can use it like a lazy reference
    private val accountListMediator: UserListsMediator by mediatorLazy()

    //----- VIEWS
    var accountNameTV: TextView? = null
    var passwordNameTV: TextView? = null

    override fun onRegister() {
        // this is a perfect place to register your notification
        registerObserver(UserProxy.NOTIFICATION_AUTH_COMPLETE) {
            println("------> NOTIFICATION AUTH COMPLETE, SIZE = ${usersList.size}")
            accountListMediator.show(LinearAnimator())
        }.registerObserver(MVVMProxy.ACCOUNT_CHANGE_HANDLER) {
            println("------> ACCOUNT PROXY social name = ${accountModel.socialName} pageId = ${accountModel.pageId}")
            accountNameTV?.text = accountModel.socialName
            passwordNameTV?.text = accountModel.pageId
        }.registerListObservers(listOf(UserProxy.NOTIFICATION_AUTH_FAILED, UserProxy.NOTIFICATION_AUTH_COMPLETE)) {
            when (it.name) {
                UserProxy.NOTIFICATION_AUTH_FAILED -> activity.toast("Login Failed").show()
                UserProxy.NOTIFICATION_AUTH_COMPLETE -> activity.toast("Login Complete").show()
            }
        }
    }

    // We can get some information from Bundle to use it in new layout view
    override fun onPrepareCreateView() {
        val name = this.arguments.getString(BUNDLE_NAME, "")
        val password = this.arguments.getString(BUNDLE_PASSWORD, "")
        log { "------->  user bundle name = $name and bundle password = $password" }
    }

    override fun createLayout(context: Context): View = UserAuthUI().createView(AnkoContext.create(context, this))

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
        sendNotification(Events.AUTH, arrayOf(accountNameTV?.text.toString(), passwordNameTV?.text.toString()))
    }

    private fun onChangeDataClicked() {
        sendNotification(Events.ACCOUNT_CHANGE, listOf("${UUID.randomUUID()}", "id_${rand(1, 100_000)}"))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_auth, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> log { "-------> SETTINGS CLICKED" }
            R.id.action_about -> log { "-------> ABOUT CLICKED" }
        }
        return super.onOptionsItemSelected(item)
    }

    fun onShowAlertPopUp() {
        activity.alert {
            title = "This is a test popup"
            message = "We test alert only for activity view lifecyrcle"

            okButton {
                it.dismiss()
            }
        }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // We can save some information to Bundle here
        this.arguments.putString(BUNDLE_NAME, accountModel.socialName)
        this.arguments.putString(BUNDLE_PASSWORD, accountModel.pageId)

        // don't forget to clear the references
        accountNameTV = null
        passwordNameTV = null
    }

    inner class UserAuthUI : AnkoComponent<MVVMMediator> {
        override fun createView(ui: AnkoContext<MVVMMediator>) = with(ui) {
            scrollView {

                verticalLayout {
                    lparams(matchParent, matchParent)
                    toolBar = toolbar {
                        setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                        title = "Login"
                        backgroundResource = R.color.colorPrimary
                    }

                    accountNameTV = editText(accountModel.socialName) {
                        hint = "Username"
                        imeOptions = EditorInfo.IME_ACTION_NEXT
                        singleLine = true
                    }

                    passwordNameTV = editText(accountModel.pageId) {
                        hint = "Password"
                        singleLine = true
                        imeOptions = EditorInfo.IME_ACTION_SEND
                        inputType = InputType.TYPE_CLASS_TEXT
                    }

                    button("Login") {
                        onClick { onLoginClicked() }
                    }

                    button("GENERATE LIVE DATA") {
                        onClick { onChangeDataClicked() }
                    }.lparams(matchParent) {
                        bottomMargin = dip16()
                    }

                    button("show view pager") {
                        onClick {
                            showMediator<ViewPagerMediator>(LinearAnimator())
                        }
                    }

                    button("show login mediator") {
                        onClick {
                            showMediator<LoginMediator>(LinearAnimator())
                        }
                    }.lparams(matchParent) {
                        bottomMargin = dip16()
                    }

                    button("show alert pop up") {
                        onClick {
                            onShowAlertPopUp()
                        }
                    }

                    button("show live data mediator") {
                        onClick {
                            showMediator<LiveDataMediator>()
                        }
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
}


