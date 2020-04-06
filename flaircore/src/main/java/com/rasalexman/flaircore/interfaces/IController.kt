package com.rasalexman.flaircore.interfaces

import androidx.collection.ArrayMap

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IController : IMultitonKey {
    /**
     * [ICommand] storage
     */
    val commandMap: ArrayMap<String, ICommand?>

    /**
     * Controller view instance
     */
    val view: IView
}

