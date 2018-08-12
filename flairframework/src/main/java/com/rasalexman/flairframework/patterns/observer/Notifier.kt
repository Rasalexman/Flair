package com.rasalexman.flairframework.patterns.observer

import com.rasalexman.flairframework.interfaces.IFacade
import com.rasalexman.flairframework.interfaces.INotifier

/**
 * Created by a.minkin on 21.11.2017.
 */
abstract class Notifier : INotifier {
    // The Multiton Key for this app
    override var multitonKey: String = ""

    override val facade: IFacade
        get() = if(multitonKey.isNotEmpty()) IFacade.core(multitonKey) else throw RuntimeException("Notifier not initialized, cause it doesn't have `multitonKey`")

}

