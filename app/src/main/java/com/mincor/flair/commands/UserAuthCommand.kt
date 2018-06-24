package com.mincor.flair.commands

import com.mincor.flair.proxies.UserProxy
import com.mincor.flairframework.interfaces.INotification
import com.mincor.flairframework.interfaces.retrieveProxy
import com.mincor.flairframework.patterns.command.SimpleCommand

/**
 * Created by a.minkin on 22.11.2017.
 */
class UserAuthCommand : SimpleCommand() {

    override fun execute(notification: INotification) {
        val userProxy: UserProxy = facade.retrieveProxy()
        val params: Array<String> = notification.body as Array<String>
        userProxy.authorization(params[0], params[1])
    }
}