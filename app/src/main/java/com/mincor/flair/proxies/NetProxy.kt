package com.mincor.flair.proxies

import com.mincor.flair.proxies.net.IWebService
import com.rasalexman.flairframework.ext.log
import com.rasalexman.flairframework.patterns.proxy.Proxy

class NetProxy(webservice: IWebService) : Proxy<IWebService>(webservice) {
    override fun onRegister() {
        super.onRegister()
        log {
            "HELLO I WEB SERVICE PROXY !!!"
        }
    }
}