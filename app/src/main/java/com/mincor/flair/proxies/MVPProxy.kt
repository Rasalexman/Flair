package com.mincor.flair.proxies

import com.mincor.flair.proxies.net.IWebService
import com.mincor.flair.views.MVPMediator
import com.rasalexman.flairframework.interfaces.proxyLazyModel
import com.rasalexman.flairframework.patterns.proxy.Proxy

class MVPProxy(view: MVPMediator) : Proxy<MVPMediator>(view) {

    private val webService by proxyLazyModel<NetProxy, IWebService>()

    override fun onRegister() {
        super.onRegister()
        data?.showFuncyMVPHandler()
    }
}