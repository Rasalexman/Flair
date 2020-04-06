package com.mincor.flair.proxies

import com.rasalexman.coroutinesmanager.doAsyncAwait
import com.rasalexman.flaircore.ext.log
import com.rasalexman.flaircoroutines.base.CoroutineAsyncProxy
import kotlin.random.Random

class WorkerProxy : CoroutineAsyncProxy<Int>(0) {

    suspend fun doSomeAsyncWork() = doAsyncAwait {
        log("THIS IS SOME HARD WORK WITH ARRAY")
        val arr = arrayListOf<Int>()
        arr.addAll(0..100000)
        val sortedarr = arr.sortedDescending()
        for (i in 0..100) {
            data = (i + Random.nextInt()) * i
        }
        log("WORK COMPLETE WITH count = $data and array = ${arr.random()} and sorted = ${sortedarr.random()}")
        if(Random.nextBoolean()) throw Exception("Hello i'm a random exception who can be created in your code")
        data
    }
}