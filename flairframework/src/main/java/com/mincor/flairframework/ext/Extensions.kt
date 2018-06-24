package com.mincor.flairframework.ext

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.mincor.flairframework.interfaces.FacadeInitializer
import com.mincor.flairframework.interfaces.IFacade
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * Create instance of a given KClass with parameters
 * It used for create an instance of Proxy/Commands/Mediators classes
 *
 * @param values
 * constructor parameters like val params = hashMapOf("field_name" to AnyGivenInstance())
 */
fun <T : Any> KClass<T>.createInstance(values: Map<String, Any> = hashMapOf()): T {
    val cons = this.primaryConstructor!!
    val valmap = cons.parameters.associateBy({ it }, {
        values[it.name]
    })
    return cons.callBy(valmap)
}

/**
 * Remove child from parent if it has
 */
fun View.removeFromParent() {
    (this.parent as? ViewGroup)?.removeView(this)
}

/**
 * Ext function
 */
fun Any.className(): String {
    val clazz = this as? KClass<*> ?: this.javaClass.kotlin
    return clazz.qualifiedName ?: clazz.java.name
}

/**
 * Instantiate new Facade for given core name
 *
 * @param key
 * Core Name, used to get IFacade instance
 *
 * @param block
 * initialization function. This is a starting point to register Proxy/Mediators/Commands
 */
fun Context.flair(key: String = IFacade.DEFAULT_KEY, block: FacadeInitializer? = null): IFacade = IFacade.core(key, this, block)

