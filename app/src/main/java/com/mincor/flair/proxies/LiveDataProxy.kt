package com.mincor.flair.proxies

import android.arch.lifecycle.MutableLiveData
import com.mincor.flair.proxies.vo.AccountModel
import com.rasalexman.flaircore.patterns.proxy.Proxy
import java.util.*

class LiveDataProxy : Proxy<MutableLiveData<AccountModel>>(MutableLiveData()) {

    override fun onRegister() {
        changeData()
    }

    fun changeData() {
        data?.value = AccountModel(UUID.randomUUID().toString(), "id_${rand(1, 100_000)}")
    }
}

fun rand(s: Int, e: Int) = Random().nextInt(e + 1 - s) + s