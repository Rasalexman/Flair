package com.mincor.flair.application

import android.app.Application
import com.mincor.flair.commands.AccountCommand
import com.mincor.flair.commands.StartupCommand
import com.mincor.flair.commands.UserAuthCommand
import com.mincor.flair.commands.WorkerCommand
import com.mincor.flair.commands.macro.LogicMacroCommand
import com.mincor.flair.events.Events.ACCOUNT_CHANGE
import com.mincor.flair.events.Events.ACCOUNT_CLEAR
import com.mincor.flair.events.Events.AUTH
import com.mincor.flair.events.Events.LOGIC_START
import com.mincor.flair.events.Events.STARTUP
import com.mincor.flair.events.Events.WORKER_EXAMPLE_EVENT
import com.rasalexman.flaircore.ext.flair
import com.rasalexman.flaircore.ext.registerCommand
import com.rasalexman.flaircore.ext.sendNotification
import com.rasalexman.flairreflect.registerCommand

/**
 * Created by a.minkin on 19.11.2017.
 */
class FlairApplication : Application() {

    companion object {
        const val CORE_FIRST = "first_core"
        const val CORE_SECOND = "second_core"
    }


    /// That's how u register your flair components
    private val flairFirstCore = flair {
        registerCommand<StartupCommand>(STARTUP)
        registerCommand<UserAuthCommand>(AUTH)
        registerCommand<AccountCommand>(ACCOUNT_CHANGE)
        registerCommand<AccountCommand>(ACCOUNT_CLEAR)
        registerCommand<WorkerCommand>(WORKER_EXAMPLE_EVENT)
        // This is a way to register components without reflections
        registerCommand(LOGIC_START) { LogicMacroCommand() }
    }

    override fun onCreate() {
        super.onCreate()
        /*
        // alternative way to register components
        flair {
            registerCommand<StartupCommand>(STARTUP)
            registerCommand<UserAuthCommand>(AUTH)
            registerCommand<AccountCommand>(ACCOUNT_CHANGE)
            registerCommand<AccountCommand>(ACCOUNT_CLEAR)
            registerCommand<LogicMacroCommand>(LOGIC_START)
            sendNotification(STARTUP)
        }*/

        flairFirstCore.sendNotification(STARTUP)
    }
}