package com.rasalexman.flairframework.patterns.observer

import com.rasalexman.flairframework.ext.className
import com.rasalexman.flairframework.interfaces.IFacade
import com.rasalexman.flairframework.interfaces.INotifier

/**
 * Created by a.minkin on 21.11.2017.
 */
abstract class Notifier(override var multitonKey: String = "") : INotifier {
    override val facade: IFacade
        get() = if(multitonKey.isNotEmpty()) IFacade.core(multitonKey) else throw RuntimeException("Notifier not initialized, cause ${this.className()} doesn't have `multitonKey`")

}

