package com.mincor.flair.proxies

import com.mincor.flair.proxies.vo.AccountModel
import com.mincor.flair.proxies.vo.UserModel
import com.rasalexman.flaircore.ext.sendNotification
import com.rasalexman.flaircore.patterns.proxy.Proxy
import com.rasalexman.flairreflect.proxyModel

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

        //sendNotification(ACCOUNT_CLEAR)

        val mainProxyData = proxyModel<MainProxy, AccountModel>()
        mainProxyData.socialName = email
        mainProxyData.pageId = pass

        addItem(UserModel("Alex", "Minkin", 30, "rastarz@yandex.ru", "efdsghghgh"))
        addItem(UserModel("Piter", "Griffin", 44, "piter@gmail.com", "dsds457dfds1224hg"))
        addItem(UserModel("Glen", "Marson", 30, "marson@yahoo.com", "sf11GH45S555"))

        sendNotification(NOTIFICATION_AUTH_COMPLETE)
    }


    /**
     * Add an item to the data.
     * @param item the userVO
     */
    private fun addItem(item: UserModel) {
        data.add(item)
    }
}