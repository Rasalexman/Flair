package com.mincor.flair.commands.macro

import com.mincor.flairframework.ext.log
import com.mincor.flairframework.interfaces.INotification
import com.mincor.flairframework.patterns.command.MacroCommand
import com.mincor.flairframework.patterns.command.SimpleCommand

class LogicMacroCommand : MacroCommand() {

    override fun initializeMacroCommand() {
        addSubCommand(object : SimpleCommand(){
            override fun execute(notification: INotification) {
                log {
                    "----->  FIRST COMMAND EXECUTE <------"
                }
            }
        })

        addSubCommand(object : SimpleCommand(){
            override fun execute(notification: INotification) {
                log {
                    "----->  SECOND COMMAND EXECUTE <------"
                }
            }
        })
    }
}