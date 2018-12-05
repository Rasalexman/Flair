package com.rasalexman.flairframework.ext

import com.rasalexman.flaircore.interfaces.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * Create instance of a given KClass with parameters
 * It used for create an instance of Proxy/Commands/Mediators classes
 *
 * @param values
 * constructor parameters as vararg
 */
fun <T : Any> KClass<T>.createInstance(values: List<Any>? = null): T {
    val cons = this.primaryConstructor!!
    val valmap = mutableMapOf<KParameter, Any>()
    if (values?.isNotEmpty() == true) {
        cons.parameters.forEachIndexed { index, kParameter ->
            valmap[kParameter] = values[index]
        }
    }
    return cons.callBy(valmap)
}

/**
 * Inject given params to constructor instance members
 */
fun Any.injectInConstructor(consParams: List<Any>? = null): Any {
    consParams?.let { params ->
        val members = this.javaClass.kotlin.memberProperties as? List<KProperty1<Any, Any>>
        members?.let {
            val ms = it.size
            params.forEachIndexed { index, param ->
                if (ms > index) {
                    val kProperty1 = it[index]
                    val paramName = param.className()
                    val propName = kProperty1.returnType.toString().replace("?", "")
                    if (paramName == propName) {
                        (kProperty1 as? KMutableProperty1<Any, Any>)?.set(this, param)
                    }
                }
            }
        }
    }
    return this
}

/**
 * Ext function
 */
fun Any.className(): String {
    val clazz = this as? KClass<*> ?: this.javaClass.kotlin
    return clazz.qualifiedName ?: clazz.java.name
}

/**
 * Register an `IProxy` core with the `Model`.
 */
inline fun <reified T : IProxy<*>> IModel.registerProxy(consParams: List<Any>? = null): T {
    return this.registerProxy { T::class.createInstance(consParams) }
}

/**
 * Register an `IMediator` core with the `View`.
 *
 * <P>
 * Registers the `IMediator` so that it can be retrieved by
 * name, and further interrogates the `IMediator` for its
 * `INotification` interests.
</P> *
 */
inline fun <reified T : IMediator> IView.registerMediator(mediatorName: String? = null): T {
    val clazz = T::class
    val clazzName = mediatorName ?: clazz.className()
    return this.registerMediator(clazzName) { clazz.createInstance() }
}