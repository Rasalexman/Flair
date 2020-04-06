package com.mincor.flair.commands.macro

import com.mincor.flair.proxies.LiveDataProxy
import com.rasalexman.flaircore.ext.log
import com.rasalexman.flaircore.interfaces.INotification
import com.rasalexman.flaircore.patterns.command.MacroCommand
import com.rasalexman.flaircore.patterns.command.SimpleCommand
import com.rasalexman.flairreflect.proxyLazy

class LogicMacroCommand : MacroCommand() {

    val liveDataProxy by proxyLazy<LiveDataProxy>()

    override fun initializeMacroCommand() {
        addSubCommand(object : SimpleCommand() {
            override fun execute(notification: INotification) {
                log {
                    " FIRST COMMAND EXECUTE <------"
                }
                liveDataProxy.changeData()
            }
        })

        addSubCommand(object : SimpleCommand() {
            override fun execute(notification: INotification) {
                log {
                    " SECOND COMMAND EXECUTE <------"
                }
            }
        })
    }
}