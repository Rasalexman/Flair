package com.mincor.flair.commands.macro

import com.mincor.flair.proxies.LiveDataProxy
import com.rasalexman.flairframework.ext.log
import com.rasalexman.flairframework.interfaces.INotification
import com.rasalexman.flairframework.interfaces.proxyLazy
import com.rasalexman.flairframework.patterns.command.MacroCommand
import com.rasalexman.flairframework.patterns.command.SimpleCommand

class LogicMacroCommand : MacroCommand() {

    val liveDataProxy by proxyLazy<LiveDataProxy>()

    override fun initializeMacroCommand() {
        addSubCommand(object : SimpleCommand() {
            override fun execute(notification: INotification) {
                log {
                    "----->  FIRST COMMAND EXECUTE <------"
                }
                liveDataProxy.changeData()
            }
        })

        addSubCommand(object : SimpleCommand() {
            override fun execute(notification: INotification) {
                log {
                    "----->  SECOND COMMAND EXECUTE <------"
                }
            }
        })
    }
}