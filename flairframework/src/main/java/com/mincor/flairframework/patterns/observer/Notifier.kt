package com.mincor.flairframework.patterns.observer

import com.mincor.flairframework.interfaces.IFacade
import com.mincor.flairframework.interfaces.INotifier
import com.mincor.flairframework.interfaces.appContext

/**
 * Created by a.minkin on 21.11.2017.
 */
abstract class Notifier : INotifier {
    // The Multiton Key for this app
    override var multitonKey: String = ""

    override val facade: IFacade
        get() = if(multitonKey.isNotEmpty()) IFacade.core(multitonKey) else throw RuntimeException("Notifier not initialized, cause doest have `multitonKey`")

}

