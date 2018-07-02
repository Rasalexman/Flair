# FlairFramework

[ ![Kotlin 1.2.50](https://img.shields.io/badge/Kotlin-1.2.50-blue.svg)](http://kotlinlang.org) [ ![Download](https://api.bintray.com/packages/sphc/FlairFramework/flair-framework/images/download.svg) ](https://bintray.com/sphc/FlairFramework/flair-framework/_latestVersion)

This is an android framework for build complex application with different architectures (MVC ready/MVP/MVVM/MVI ets). It's create on top of MVC pattern with powerful event system and property delegation, also it support multi-core instances and animation changes between views (see example project for more information). 

The start point for initialize framework is declare 'flair' instance in onCreate method in MainApplication file. But u can initialize framework in any part of ur project such as `FlairActivity` or any `Context` implementations
```kotlin
val flairCoreInstance = flair {
        registerCommand<MyCommand>(eventName)
        registerProxy<MyProxy>()
        registerMediator<MyMediator>()
    }
```
The second point or using 'Flare' is attach created core to single Activity class and root layout container (but u can no specify any root container and flair take it for you automatically as `activity.window.decorView.findViewById(android.R.id.content)`). Important thing: only one activity (that should be an instance of FlairActivity) can be stored in one core of FlairFramework
```kotlin
class MainActivity : FlairActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val showingAnimation = LinearAnimator()
        flair().attach(this).showLastOrExistMediator<MyMediator>(showingAnimation)
        // or 
        // val rootContainer = frameLayout()
        // flair().attach(this, rootContainer).retrieveMediator<MyMediator>().show()
    }
}
```

Components:
1) 'flair' instance is a simple IFacade singleton instance as core functionality point
2) SimpleCommand instances is a command pattern realisation
3) Proxy objects is a complex object that store data to manipulate with, it's like repository for ur network calls or database
4) Mediator is a simple view-hierarchy handler class, it's store and manage lifecyrcle of your view components such as AnkoComponents or xml-layout files. Also it support powerfull view backstack storage.
5) Also you has LinearAnimator.kt for create simple view animation changes such as HorizontalAnimation, or u can extends LinearAnimator and create ur own realisation. 
6) All components of a FlairFramework are linked together by a powerful messaging system. You can notify every part of your system by calling `sendNotification(event, data)`. Mediator can notify commands, commands can notify mediators and another commands, proxy can notify mediators and another commands. 

Mediators can handle notification by
```kotlin
class MyMediator : Mediator() {
  // called when medaitor is registered
  override fun onRegister() {
      // register notification for this IMediator instance
      registerObserver(eventName:String) {
            // event handler
      }
      sendNotification(eventName)
  }
  
  // the way to create UI
  override fun onCreateView(context: Context) {
        viewComponent = UserAuthUI().createView(AnkoContext.create(context, this))
        // or you can inflate you custom xml layout
        // viewComponetn = inflateView(R.layout.simple_layout)
  }
}
```

Proxy object can recieve notification by linked commands as usecases
```kotlin
class MyProxy : Proxy<String>("data_to_store_in_proxy") {
    fun handleNotification() {
        Low.d("HELLO FROM PROXY")
    }
}
//the command that controls the proxy
class MyCommand : SimpleCommand() {
    val myProxy by proxy<MyProxy>()
    override fun execute(notification: INotification) {
         myProxy.handleNotification()     
    }
}
```

See the sample project `app` for more complex information. Also code base has good comments on every functions

Maven:
```
<dependency>
  <groupId>com.rasalexman.flairframework</groupId>
  <artifactId>flairframework</artifactId>
  <version>x.y.z</version>
  <type>pom</type>
</dependency>
```

Gradle:
```
implementation 'com.rasalexman.flairframework:flairframework:x.y.z'
```


TODO:
+ Try to add full support for DEPENDENCY INJECTION
+ Improve backstack navigation
