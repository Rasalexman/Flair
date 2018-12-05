package com.rasalexman.flaircore.interfaces

/**
 * Created by alexander on 23.03.2018.
 */
interface IMultitonKey {

    /**
     * Initialize this INotifier core.
     * <P>
     * This is how a Notifier gets its multitonKey.
     * Calls to sendNotification or to access the
     * facade will fail until after this property
     * has no initialized.</P>
     *
     * <P>
     * Mediators, Commands or Proxies may override
     * this property in order to send notifications
     * or access the Multiton Facade core as
     * soon as possible. They CANNOT access the facade
     * in their constructors, since this property will not
     * yet have been initialized.</P>
     */
    var multitonKey: String
}