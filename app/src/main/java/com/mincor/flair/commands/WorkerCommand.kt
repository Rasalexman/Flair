package com.mincor.flair.commands

import com.mincor.flair.proxies.WorkerProxy
import com.rasalexman.coroutinesmanager.launchOnUITryCatchFinally
import com.rasalexman.flaircore.interfaces.INotification
import com.rasalexman.flaircore.interfaces.sendNotification
import com.rasalexman.flaircoroutines.base.CoroutineCommand
import com.rasalexman.flairreflect.proxyLazy

class WorkerCommand : CoroutineCommand() {

    private val workerProxy by proxyLazy<WorkerProxy>()

    override fun execute(notification: INotification) {
        // Do not forget to stop your coroutines before doing new work
        // workerProxy.cancelAllCoroutines()
        // cancelAllCoroutines()
        launchCoroutineWork()
    }

    private fun launchCoroutineWork() = launchOnUITryCatchFinally({
        val count = workerProxy.doSomeAsyncWork()
        sendNotification(WORK_COMPLETE_EVENT, "It $count")
    }, {
        println("-----> There is an exception with try block $it")
        sendNotification(WORK_COMPLETE_EVENT, "It FALSE")
    }, {
        println("-----> THIS IS A FINAL BLOCK")
    })

    companion object {
        const val WORK_COMPLETE_EVENT = "work_complete_event"
    }
}