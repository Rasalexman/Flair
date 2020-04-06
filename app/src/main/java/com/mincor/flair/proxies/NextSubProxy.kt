package com.mincor.flair.proxies

import com.mincor.flair.views.subcomponents.NextSubChildMediator
import com.rasalexman.flaircore.patterns.proxy.Proxy

class NextSubProxy(view: NextSubChildMediator) : Proxy<NextSubChildMediator?>(view) {

    fun callViewNameToMediator() {
        data?.onProxyCalledHandler(data?.mediatorName)
    }

    interface IView {
        var mediatorName: String?
        fun onProxyCalledHandler(str: String?)
    }
}