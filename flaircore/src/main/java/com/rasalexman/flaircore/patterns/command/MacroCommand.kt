package com.rasalexman.flaircore.patterns.command

import com.rasalexman.flaircore.interfaces.ICommand
import com.rasalexman.flaircore.interfaces.INotification
import com.rasalexman.flaircore.patterns.observer.Notifier


/**
 * Created by a.minkin on 21.11.2017.
 */
abstract class MacroCommand : Notifier(), ICommand {

    private val subCommands: MutableList<ICommand> = mutableListOf()

    /**
     * Initialize the `MacroCommand`.
     *
     * <P>
     * In your subclass, override this method to initialize the
     * `MacroCommand`'s *SubCommand* list with
     * `ICommand` class references like this:
    </P> *
     *
     * <listing> // Initialize MyMacroCommand override protected function
     * initializeMacroCommand( ) : void { addSubCommand(
     * com.me.myapp.controller.FirstCommand ); addSubCommand(
     * com.me.myapp.controller.SecondCommand ); addSubCommand(
     * com.me.myapp.controller.ThirdCommand ); } </listing>
     *
     * <P>
     * Note that *SubCommand*s may be any `ICommand`
     * implementor, `MacroCommand`s or `SimpleCommands`
     * are both acceptable.
    </P> */
    open fun initializeMacroCommand() {}

    /**
     * Add a *SubCommand*.
     *
     * <P>
     * The *SubCommands* will be called in First In/First Out (FIFO)
     * order.
    </P> *
     *
     * @param commandClassRef
     * a reference to the `Class` of the
     * `ICommand`.
     */
    protected fun addSubCommand(commandClassRef: ICommand) {
        this.subCommands.add(commandClassRef)
    }

    /**
     * Execute this `MacroCommand`'s *SubCommands*.
     *
     * <P>
     * The *SubCommands* will be called in First In/First Out (FIFO)
     * order.
     *
     * @param notification
     * the `INotification` object to be passed to each
     * *SubCommand*.
    </P> */
    override fun execute(notification: INotification) {
        subCommands.forEach {
            it.multitonKey = multitonKey
            it.execute(notification)
        }
    }

    fun clear() {
        subCommands.clear()
    }
}