package com.mincor.flair.proxies

import com.mincor.flair.proxies.net.IWebService
import com.mincor.flair.views.MVPMediator
import com.rasalexman.flaircore.interfaces.registerObserver
import com.rasalexman.flaircore.patterns.proxy.Proxy
import com.rasalexman.flairreflect.proxyLazyModel

class MVPProxy(view: MVPMediator) : Proxy<MVPMediator>(view) {

    private val webService by proxyLazyModel<NetProxy, IWebService>()

    override fun onRegister() {
        super.onRegister()
        data.showFuncyMVPHandler()

        facade.registerObserver("some_event") {

        }
    }
}