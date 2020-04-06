package com.rasalexman.flaircore.interfaces

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IController : IMultitonKey {
    /**
     * [ICommand] storage
     */
    val commandMap: HashMap<String, ICommand?>

    /**
     * Controller view instance
     */
    val view: IView
}

