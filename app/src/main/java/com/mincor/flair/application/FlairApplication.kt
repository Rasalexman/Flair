package com.mincor.flair.application

import android.app.Application
import com.mincor.flair.commands.AccountCommand
import com.mincor.flair.commands.StartupCommand
import com.mincor.flair.commands.UserAuthCommand
import com.mincor.flair.commands.macro.LogicMacroCommand
import com.mincor.flair.events.Events.ACCOUNT_CHANGE
import com.mincor.flair.events.Events.ACCOUNT_CLEAR
import com.mincor.flair.events.Events.AUTH
import com.mincor.flair.events.Events.LOGIC_START
import com.mincor.flair.events.Events.STARTUP
import com.mincor.flairframework.ext.flair
import com.mincor.flairframework.interfaces.registerCommand
import com.mincor.flairframework.interfaces.sendNotification

/**
 * Created by a.minkin on 19.11.2017.
 */
class FlairApplication : Application() {

    companion object {
        const val CORE_FIRST = "first_core"
        const val CORE_SECOND = "second_core"
    }


    /// That's how u register ur flair components
    private val flairFirstCore = flair {
        registerCommand<StartupCommand>(STARTUP)
        registerCommand<UserAuthCommand>(AUTH)
        registerCommand<AccountCommand>(ACCOUNT_CHANGE)
        registerCommand<AccountCommand>(ACCOUNT_CLEAR)
        registerCommand<LogicMacroCommand>(LOGIC_START)
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