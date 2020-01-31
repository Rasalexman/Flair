package com.mincor.flair.proxies

import com.rasalexman.coroutinesmanager.doAsyncAwait
import com.rasalexman.flaircoroutines.base.CoroutineAsyncProxy
import kotlin.random.Random

class WorkerProxy : CoroutineAsyncProxy<Any>() {

    suspend fun doSomeAsyncWork() = doAsyncAwait {
        println("-----> THIS IS SOME HARD WORK WITH ARRAY")
        val arr = arrayListOf<Int>()
        arr.addAll(0..100000)
        val sortedarr = arr.sortedDescending()
        var count = 0
        for (i in 0..100) {
            count = (i + Random.nextInt()) * i
        }
        println("-----> WORK COMPLETE WITH count = $count and array = ${arr.random()} and sorted = ${sortedarr.random()}")
        if(Random.nextBoolean()) throw Exception("Hello i'm a random exception who can be created in your code")
        count
    }
}