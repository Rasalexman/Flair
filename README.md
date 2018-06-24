# Flair
This is an android framework for build complex application with different architectures (MVP/MVVM/MVI ets). It's create on top of MVC pattern with powerful event system and property delegation, also it support multi-core instances and animation changes between views (see example project for more information). 

The start point for initialize framework is declare 'flair' instance in onCreate method in MainApplication file. But u can initialize framework in any part of ur project
```kotlin
val flairCoreInstance = flair {
        registerCommand<object:SimpleCommand>(eventName)
        registerProxy<object:Proxy>()
        registerMediator<object:Mediator>()
    }
```
Components:
1) 'flair' instance is a simple IFacade instace as core functionality point
2) SimpleCommand instances is a command pattern realisation
3) Proxy objects is a complex object that store data to manipulate with, it's like repository for ur network calls or database
4) Mediator is a simple view-hierarchy handler class, it's store and manage lifecyrcle of your view components such as AnkoComponents or xml-layout files. Also it support powerfull view backstack storage.
5) Also you has LinearAnimator.kt for create simple view animation changes such as HorizontalAnimation, or u can extends LinearAnimator and create ur own realisation. 
6) All components of a FlairFramework are linked together by a powerful messaging system. You can notify every part of your system by calling `sendNotification(event, data)`. Mediator can notify commands, commands can notify mediators and another commands, proxy can notify mediators and another commands. 

Mediators can handle notification by
```kotlin
class SimpleMediator : Mediator() {
  // called when medaitor is registered
  override fun onRegister() {
      // register notification for this IMediator instance
      registerObserver(eventName:String) {
            // event handler
        }
  }
}
```
