package com.mincor.flair.commands

import com.mincor.flair.events.Events.ACCOUNT_CHANGE
import com.mincor.flair.events.Events.ACCOUNT_CLEAR
import com.mincor.flair.proxies.MainProxy
import com.rasalexman.flaircore.interfaces.INotification
import com.rasalexman.flaircore.interfaces.proxyLazy
import com.rasalexman.flaircore.patterns.command.SimpleCommand

class AccountCommand : SimpleCommand() {

    private val accountProxy by proxyLazy<MainProxy>()

    override fun execute(notification: INotification) {

        when (notification.name) {
            ACCOUNT_CHANGE -> {
                val body = notification.body as? List<String>
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