package com.mincor.flair.views

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.mincor.flair.R
import com.mincor.flair.events.Events
import com.mincor.flair.proxies.LiveDataProxy
import com.mincor.flair.proxies.vo.AccountModel
import com.rasalexman.flairframework.interfaces.proxyLazyModel
import com.rasalexman.flairframework.interfaces.sendNotification
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk25.coroutines.onClick

class LiveDataMediator : ToolbarMediator(), Observer<AccountModel> {

    override fun createLayout(context: Context): View = LiveDataUI().createView(AnkoContext.create(context, this))

    private val liveData by proxyLazyModel<LiveDataProxy, MutableLiveData<AccountModel>>()

    private var socialNameTV:TextView? = null
    private var pageIdTV:TextView? = null

    override fun onAddedView(view: View) {
        super.onAddedView(view)
        setHomeButtonEnable()
        onChanged(liveData.value)
        liveData.observeForever(this)
    }

    override fun onRemovedView(view: View) {
        super.onRemovedView(view)
        liveData.removeObserver(this)
        socialNameTV = null
        pageIdTV = null
    }

    override fun onChanged(t: AccountModel?) {
        socialNameTV?.text = t?.socialName
        pageIdTV?.text = t?.pageId
    }

    inner class LiveDataUI : AnkoComponent<LiveDataMediator> {
        override fun createView(ui: AnkoContext<LiveDataMediator>): View = with(ui) {
            verticalLayout {
                lparams(matchParent)

                toolBar = toolbar {
                    setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                    title = "Live Data Mediator"
                    backgroundResource = R.color.colorPrimary
                }

                textView("Social name:") {
                    textSize = 16f
                    textColor = Color.LTGRAY
                }.lparams(matchParent) {
                    setMargins(dip(16), dip(16), dip(16), 0)
                }

                socialNameTV = textView {
                    textSize = 22f
                    textColor = Color.BLACK
                }.lparams(matchParent) {
                    margin = dip(16)
                }

                textView("PageID:") {
                    textSize = 16f
                    textColor = Color.LTGRAY
                }.lparams(matchParent) {
                    setMargins(dip(16), dip(16), dip(16), 0)
                }

                pageIdTV = textView {
                    textSize = 22f
                    textColor = Color.BLACK
                }.lparams(matchParent) {
                    margin = dip(16)
                }

                button("Change data") {
                    onClick {
                        sendNotification(Events.LOGIC_START)
                    }
                }.lparams(dip(160)) {
                    gravity = Gravity.CENTER_HORIZONTAL
                }

            }

        }
    }
}