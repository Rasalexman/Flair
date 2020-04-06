package com.rasalexman.flaircore.patterns.observer

import com.rasalexman.flaircore.interfaces.IFacade
import com.rasalexman.flaircore.interfaces.INotifier

/**
 * Created by a.minkin on 21.11.2017.
 *
 * @param multitonKey - flair facade core name
 */
abstract class Notifier(override var multitonKey: String = "") : INotifier {

    /**
     * get [IFacade] instance for this core and [multitonKey]
     */
    override val facade: IFacade
        get() = if(multitonKey.isNotEmpty()) IFacade.core(multitonKey) else throw RuntimeException("Notifier not initialized, cause $this doesn't have `multitonKey`")

}

