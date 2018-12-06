package com.rasalexman.flairreflect

import com.rasalexman.flaircore.interfaces.*
import com.rasalexman.flaircore.interfaces.IAnimator
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
 * Retrieve an `IProxy` core from the Model.
 *
 * @return the `IProxy` core previously registered
 */
inline fun <reified T : IProxy<*>> IModel.retrieveProxy(params: List<Any>? = null): T = this.proxyMap[T::class.toString()]?.injectInConstructor(params) as? T
        ?: registerProxy(params)

/**
 * Retrieve lazy proxy core or create new one if it does not has, by given generic class
 *
 * @param dataToHold
 * Constructor parameters
 */
inline fun <reified T : IProxy<*>> INotifier.proxyLazy(vararg dataToHold: Any): Lazy<T> = lazy {
    if (facade.hasProxy<T>()) facade.model.retrieveProxy<T>(dataToHold.asList()) else this.facade.model.registerProxy(dataToHold.asList())
}

/**
 * Retrieve lazy proxyModel data by given generic class
 */
inline fun <reified T : IProxy<*>, reified R : Any> INotifier.proxyLazyModel(): Lazy<R> = lazy {
    proxyModel<T, R>()
}

/**
 * Retrieve proxyModel data by given generic class
 */
inline fun <reified T : IProxy<*>, reified R : Any> INotifier.proxyModel(): R = facade.model.retrieveProxy<T>().data as R

/**
 * Retrieve proxy core or create new one if it does not has, by given generic class
 *
 * @param dataToHold
 * Constructor parameters
 */
inline fun <reified T : IProxy<*>> INotifier.proxy(vararg dataToHold: Any): T = facade.model.retrieveProxy(dataToHold.asList())

/**
 * Register an `ICommand` with the `Controller`.
 *
 * @param noteName
 * the name of the `INotification` to associate the
 * `ICommand` with.
 *
 * @param commandBuilder
 * The builder function to instantiate instance of ICommand class
 */
inline fun <reified T : ICommand> IFacade.registerCommand(noteName: String) {
    this.controller.registerCommand(noteName) { T::class.createInstance() }
}

/**
 * Register an `IProxy` with the `Model` by name.
 *
 * @param dataToHold
 * Contructor parameters that proxyLazy must apply
 *
 * @return IProxy instance with given parameters
 */
inline fun <reified T : IProxy<*>> IFacade.registerProxy(vararg params: Any): T = this.model.registerProxy(params.toList())


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
    val clazzName = mediatorName ?: clazz.toString()
    return this.registerMediator(clazzName) { clazz.createInstance() }
}

/**
 * Register an `IMediator` with the `View` core.
 *
 * @param mediatorName
 * The name of the mediator to register with
 */
inline fun <reified T : IMediator> IFacade.registerMediator(mediatorName: String? = null) = this.view.registerMediator<T>(mediatorName)

/**
 * Retrieve lazy mediator core by given generic class
 *
 * @param mediatorName
 * Mediator name to be retrieved
 */
inline fun <reified T : IMediator> IMediator.mediator(mediatorName: String? = null): T {
    val name = mediatorName ?: T::class.toString()
    return if (!this.facade.view.hasMediator<T>(name)) this.facade.registerMediator(name)
    else this.facade.retrieveMediator(name)
}

/**
 * Show the given generic mediatorLazy or by name
 *
 * @param mediatorName
 * Mediator name to be showed
 */
inline fun <reified T : IMediator> IMediator.showMediator(animation: IAnimator? = null, mediatorName: String? = null) {
    val name = mediatorName ?: T::class.toString()
    return if (!this.facade.view.hasMediator<T>(name)) this.facade.registerMediator<T>(name).show(animation)
    else this.facade.retrieveMediator<T>(name).show(animation)
}

/**
 * Retrieve lazy mediator core by given generic class
 *
 * @param mediatorName
 * Mediator name to be retrieved by lazy function
 */
inline fun <reified T : IMediator> IMediator.mediatorLazy(mediatorName: String? = null): Lazy<T> = lazy {
    val name = mediatorName ?: T::class.toString()
    if (!this.facade.view.hasMediator<T>(name)) this.facade.registerMediator<T>(name)
    this.facade.retrieveMediator<T>(name)
}

/**
 * Show last added IMediator from backstack. If there is no mediator in backstack show the one passed by generic type class
 *
 * @param animation
 * The animation instance to show last mediator
 */
inline fun <reified T : IMediator> IFacade.showLastOrExistMediator(animation: IAnimator? = null, mediatorName: String? = null) {
    val name = mediatorName ?: T::class.toString()
    if (!view.hasMediator<T>(name)) registerMediator<T>(name)
    view.showLastOrExistMediator<T>(animation)
}