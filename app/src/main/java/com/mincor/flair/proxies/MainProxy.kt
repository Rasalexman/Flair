package com.mincor.flair.proxies

import com.mincor.flair.proxies.vo.AccountModel
import com.rasalexman.flaircore.ext.sendNotification
import com.rasalexman.flaircore.patterns.proxy.Proxy
import kotlin.properties.Delegates

class MainProxy : Proxy<AccountModel>(AccountModel("", "")) {

    var socialName: String by Delegates.observable("") { cur, old, new ->
        println("-------> socialName: current value $cur OLD value $old NEW VALUE $new")
        data.socialName = new
        sendNotification(ACCOUNT_CHANGE_HANDLER)
    }

    var pageId: String by Delegates.observable("") { cur, old, new ->
        println("--------> pageId: current value $cur OLD value $old NEW VALUE $new")
        data.pageId = new
        sendNotification(ACCOUNT_CHANGE_HANDLER)
    }

    companion object {
        const val ACCOUNT_CHANGE_HANDLER = "account_change_Handler"
    }

    fun changeAccount(name: String, id: String) {
        this.socialName = name
        this.pageId = id
    }

    fun clear() {
        this.socialName = ""
        this.pageId = ""
    }
}