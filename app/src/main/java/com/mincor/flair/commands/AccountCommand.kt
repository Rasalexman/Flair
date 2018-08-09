package com.mincor.flair.commands

import com.mincor.flair.events.Events.ACCOUNT_CHANGE
import com.mincor.flair.events.Events.ACCOUNT_CLEAR
import com.mincor.flair.proxies.MVVMProxy
import com.rasalexman.flairframework.interfaces.INotification
import com.rasalexman.flairframework.interfaces.proxyLazy
import com.rasalexman.flairframework.patterns.command.SimpleCommand

class AccountCommand : SimpleCommand() {

    private val accountProxy by proxyLazy<MVVMProxy>()

    override fun execute(notification: INotification) {

        when (notification.name) {
            ACCOUNT_CHANGE -> {
                val body = notification.body as? ArrayList<String>
                body?.let {
                    accountProxy.changeAccount(body[0], body[1])
                }
            }
            ACCOUNT_CLEAR -> {
                accountProxy.clear()
            }
        }
    }
}