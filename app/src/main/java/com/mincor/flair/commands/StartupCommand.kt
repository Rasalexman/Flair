package com.mincor.flair.commands

import android.content.Context
import android.net.ConnectivityManager
import com.mincor.flair.events.Events
import com.mincor.flair.proxies.NetProxy
import com.mincor.flair.utils.createOkHttpClient
import com.mincor.flair.utils.createWebServiceApi
import com.mincor.flair.utils.takeDeviceID
import com.mincor.flair.views.MVPMediator
import com.mincor.flair.views.MVVMMediator
import com.mincor.flair.views.UserListsMediator
import com.rasalexman.flaircore.interfaces.*
import com.rasalexman.flaircore.patterns.command.SimpleCommand
import com.rasalexman.flairreflect.registerMediator

/**
 * Created by a.minkin on 21.11.2017.
 */
class StartupCommand : SimpleCommand() {

    override fun execute(notification: INotification) {

        val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val resolver = appContext.contentResolver

        // proxyLazy with web service Retrofit
        facade.registerProxy {
            NetProxy(createWebServiceApi(createOkHttpClient(cm, takeDeviceID(resolver)),"https://app.dp.ru/api/v1.0/"))
        }

        //facade.registerProxy<MVVMProxy>()
        //facade.registerProxy<LiveDataProxy>()
        facade.registerMediator<MVPMediator>()
        facade.registerMediator<MVVMMediator>()
        facade.registerMediator<UserListsMediator>()

        // send some notification to start logic case
        // sendNotification(Events.LOGIC_START)

        // remove unused commands
        facade.removeCommand(Events.STARTUP)
        //flair().removeCommand(Events.LOGIC_START)

        //check to execute again
        //sendNotification(Events.LOGIC_START)
    }
}