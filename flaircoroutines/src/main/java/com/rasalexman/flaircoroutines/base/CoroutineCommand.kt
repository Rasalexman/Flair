package com.rasalexman.flaircoroutines.base

import com.rasalexman.coroutinesmanager.ICoroutinesManager
import com.rasalexman.flaircore.patterns.command.SimpleCommand

/**
 * Base Command Async class
 * Used for async{} block calls
 */
abstract class CoroutineCommand : SimpleCommand(), ICoroutinesManager