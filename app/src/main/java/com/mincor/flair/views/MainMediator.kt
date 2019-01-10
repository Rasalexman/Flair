package com.mincor.flair.views

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.mincor.flair.R
import com.mincor.flair.commands.WorkerCommand
import com.mincor.flair.events.Events
import com.mincor.flair.proxies.MainProxy
import com.mincor.flair.proxies.UserProxy
import com.mincor.flair.proxies.rand
import com.mincor.flair.proxies.vo.AccountModel
import com.mincor.flair.proxies.vo.UserModel
import com.mincor.flair.utils.Keyboards
import com.mincor.flair.utils.dip16
import com.mincor.flair.views.auth.LoginMediator
import com.rasalexman.flaircore.animation.FadeAnimator
import com.rasalexman.flaircore.animation.LinearAnimator
import com.rasalexman.flaircore.ext.log
import com.rasalexman.flaircore.interfaces.*
import com.rasalexman.flairreflect.mediatorLazy
import com.rasalexman.flairreflect.proxyLazyModel
import com.rasalexman.flairreflect.showMediator
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

/**
 * Created by a.minkin.
 */
class MainMediator : ToolbarMediator() {

    companion object {
        const val BUNDLE_NAME = "user_name"
        const val BUNDLE_PASSWORD = "user_password"
    }

    override var hasOptionalMenu: Boolean = true

    // model as AccountModel
    private val accountModel by proxyLazyModel<MainProxy, AccountModel>()
    // models as List
    private val usersList by proxyLazyModel<UserProxy, MutableList<UserModel>>()
    // you can use it like a lazy reference
    private val accountListMediator: UserListsMediator by mediatorLazy()

    //----- VIEWS
    var accountNameTV: EditText? = null
    var passwordNameTV: EditText? = null

    override fun onRegister() {
        // this is a perfect place to register your notification
        registerObserver(UserProxy.NOTIFICATION_AUTH_COMPLETE) {
            println("------> NOTIFICATION AUTH COMPLETE, SIZE = ${usersList.size}")
            addSomeDataInBundle()
            accountListMediator.show(LinearAnimator())
        }.registerObserver(WorkerCommand.WORK_COMPLETE_EVENT) {
            activity.toast("WORK COMPLETE WITH ${it.body}")
        }.registerObserver(MainProxy.ACCOUNT_CHANGE_HANDLER) {
            println("------> ACCOUNT PROXY social name = ${accountModel.socialName} pageId = ${accountModel.pageId}")
            accountNameTV?.setText(accountModel.socialName)
            passwordNameTV?.setText(accountModel.pageId)
        }.registerListObservers(listOf(UserProxy.NOTIFICATION_AUTH_FAILED, UserProxy.NOTIFICATION_AUTH_COMPLETE)) {
            when (it.name) {
                UserProxy.NOTIFICATION_AUTH_FAILED -> activity.toast("Login Failed")
                UserProxy.NOTIFICATION_AUTH_COMPLETE -> activity.toast("Login Complete")
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

    override fun onAddedView(view: View) {
        super.onAddedView(view)
        accountNameTV?.setText(this.arguments.getString(BUNDLE_NAME, ""))
        passwordNameTV?.setText(this.arguments.getString(BUNDLE_PASSWORD, ""))
    }

    private fun onLoginClicked() {
        viewComponent?.let {
            Keyboards.hideKeyboard(it.context, it)
        }
        println("-----> account first data $accountModel")
        sendNotification(Events.AUTH, arrayOf(accountNameTV?.text.toString(), passwordNameTV?.text.toString()))
    }

    private fun onAsyncWorkClicked() {
        activity.toast("START ASYNC WORK")
        sendNotification(Events.WORKER_EXAMPLE_EVENT)
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
            message = "We test alert only for activity view lifecycle"

            okButton {
                it.dismiss()
            }
        }.show()
    }

    override fun onAnimationStart(isShow: Boolean) {
        super.onAnimationStart(isShow)
        addSomeDataInBundle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        addSomeDataInBundle()
        // don't forget to clear the references
        accountNameTV = null
        passwordNameTV = null
    }

    private fun addSomeDataInBundle() {
        // We can save some information to Bundle here
        this.arguments.putString(BUNDLE_NAME, accountModel.socialName)
        this.arguments.putString(BUNDLE_PASSWORD, accountModel.pageId)
    }

    class UserAuthUI : AnkoComponent<MainMediator> {
        override fun createView(ui: AnkoContext<MainMediator>) = with(ui) {
            scrollView {

                verticalLayout {
                    lparams(matchParent, matchParent)
                    ui.owner.toolBar = toolbar {
                        setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                        title = "Login"
                        backgroundResource = R.color.colorPrimary
                    }

                    ui.owner.accountNameTV = editText(ui.owner.accountModel.socialName) {
                        hint = "Username"
                        imeOptions = EditorInfo.IME_ACTION_NEXT
                        singleLine = true
                    }

                    ui.owner.passwordNameTV = editText(ui.owner.accountModel.pageId) {
                        hint = "Password"
                        singleLine = true
                        imeOptions = EditorInfo.IME_ACTION_SEND
                        inputType = InputType.TYPE_CLASS_TEXT
                    }

                    button("Login") {
                        onClick { ui.owner.onLoginClicked() }
                    }

                    button("Async Work") {
                        onClick { ui.owner.onAsyncWorkClicked() }
                    }

                    button("GENERATE LIVE DATA") {
                        onClick { ui.owner.onChangeDataClicked() }
                    }.lparams(matchParent) {
                        bottomMargin = dip16()
                    }

                    button("show view pager") {
                        onClick {
                            ui.owner.showMediator<ViewPagerMediator>(FadeAnimator())
                        }
                    }

                    button("show login mediator") {
                        onClick {
                            ui.owner.showMediator<LoginMediator>(LinearAnimator())
                        }
                    }.lparams(matchParent) {
                        bottomMargin = dip16()
                    }

                    button("show alert pop up") {
                        onClick {
                            ui.owner.onShowAlertPopUp()
                        }
                    }

                    button("show live data mediator") {
                        onClick {
                            ui.owner.showMediator<LiveDataMediator>()
                        }
                    }

                    button("SHOW SUB CHILD MEDIATOR") {
                        onClick {
                            ui.owner.showMediator<AnotherCoreMediator>()
                        }
                    }
                }
            }
        }
    }


}


