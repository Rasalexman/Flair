package com.mincor.flairframework.patterns.command

import com.mincor.flairframework.interfaces.ICommand
import com.mincor.flairframework.interfaces.INotification
import com.mincor.flairframework.patterns.observer.Notifier

/**
 * Created by a.minkin on 21.11.2017.
 */
abstract class SimpleCommand : Notifier(), ICommand {
    /**
     * Fulfill the use-case initiated by the given `INotification`.
     *
     * <P>
     * In the Command Pattern, an application use-case typically begins with
     * some user action, which results in an `INotification` being
     * broadcast, which is handled by business logic in the `execute`
     * method of an `ICommand`.
    </P> *
     *
     * @param notification
     * the `INotification` to handle.
     */
    override fun execute(notification: INotification) {}
}