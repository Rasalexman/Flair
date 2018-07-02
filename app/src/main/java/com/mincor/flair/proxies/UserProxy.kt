package com.mincor.flair.proxies

import com.mincor.flair.activity.log
import com.mincor.flair.events.Events.ACCOUNT_CLEAR
import com.mincor.flair.proxies.vo.UserModel
import com.mincor.flairframework.interfaces.sendNotification
import com.mincor.flairframework.patterns.proxy.Proxy
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

/**
 * Created by a.minkin on 21.11.2017.
 */
class UserProxy : Proxy<MutableList<UserModel>>(mutableListOf()) {

    companion object {
        const val NOTIFICATION_AUTH_COMPLETE = "AUTH_COMPLETE"
        const val NOTIFICATION_AUTH_FAILED = "AUTH_FAILED"
    }

    /**
     * Authorization User
     */
    fun authorization(email: String, pass: String) {

        // some coroutine scopes
        launch(UI){
            val result = async(CommonPool){
                // do some network call or
                var counter = 0
                repeat(5){
                    log { "-----> email $email pass $pass" }
                    counter++
                }
                counter
            }

            log { "${result.await()} count times " }
        }

       sendNotification(ACCOUNT_CLEAR)

        addItem(UserModel("Alex", "Minkin", 30, "rastarz@yandex.ru", "efdsghghgh"))
        addItem(UserModel("Piter", "Griffin", 44, "piter@gmail.com", "dsds457dfds1224hg"))
        addItem(UserModel("Glen", "Marson", 30, "marson@yahoo.com", "sf11GH45S555"))

        sendNotification(NOTIFICATION_AUTH_COMPLETE)
    }


    /**
     * Add an item to the data.
     * @param item the userVO
     */
    fun addItem(item: UserModel) {
        data?.add(item)
    }
}