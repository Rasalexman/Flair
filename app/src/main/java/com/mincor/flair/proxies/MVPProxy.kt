package com.mincor.flair.proxies

import com.mincor.flair.proxies.net.IWebService
import com.mincor.flair.views.MVPMediator
import com.mincor.flairframework.interfaces.proxyLazyModel
import com.mincor.flairframework.patterns.proxy.Proxy
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.awaitResult

class MVPProxy(view: MVPMediator) : Proxy<MVPMediator>(view) {

    private val webService by proxyLazyModel<NetProxy, IWebService>()

    override fun onRegister() {
        super.onRegister()
        data?.showFuncyMVPHandler()
    }

    fun lazynessFunctionCall(){
        launch(UI){
            delay(2000)

            /*val result = withContext(CommonPool) {
                webService.getTags().awaitResult()
            }
            when(result) {
                is Result.Ok -> {
                    val response = result.value
                    response.error?.let {
                        data?.showErrorHandler(it.message?:"")
                    }?:response.response?.let {
                        data?.coroutinesResponceHander(it.tags?.added)
                    }
                }
                is Result.Error -> {
                    data?.showErrorHandler("SOME RESULT ERROR")
                }
                is Result.Exception -> {
                    data?.showErrorHandler("SOME RESULT EXCEPTION")
                }
            }*/
        }
    }
}