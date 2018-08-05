# FlairFramework

[ ![Kotlin 1.2.60](https://img.shields.io/badge/Kotlin-1.2.60-blue.svg)](http://kotlinlang.org) [ ![Download](https://api.bintray.com/packages/sphc/FlairFramework/flair-framework/images/download.svg) ](https://bintray.com/sphc/FlairFramework/flair-framework/_latestVersion)

This is an android framework for build complex application with different architectures (MVC ready/MVP/MVVM/MVI ets). It's create on top of MVC pattern with powerful event system, dependency injection and property delegation, also it support multi-core instances and animation changes between views (see example project for more information). 
The `FlairFramework` is easy to use, it's light-weight, extensible, flexible and it's has more simplier view lifecyrcle than Fragments and Activities

The start point for initialize framework is declare 'flair' instance in onCreate method in MainApplication file. But u can initialize framework in any part of ur project such as `FlairActivity` or any `Context` implementations
```kotlin
val flairCoreInstance = flair {
        registerCommand<MyCommand>(eventName)
        registerProxy<MyProxy>()
        registerMediator<MyMediator>()
    }
```

You can register all part of Flair framework in any part of your application by calling lazy functions or inline functions like `proxy()`, `proxyLazy()`, `mediator()`, `mediatorLazy()`

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
2) SimpleCommand instances is a command pattern realisation. You can manipulate proxy objects from it's instance as like usecases
3) MacroCommands can combine more than one SimpleCommand and execute it one by one
4) Proxy objects is a complex object that store data to manipulate with, it's like repository for ur network calls or database
5) Mediator is a simple view-hierarchy handler class, it's store and manage life cicle of your view components such as AnkoComponents or xml-layout files. Also it support view backstack storage.
6) Also you has `LinearAnimator.kt` for create simple view animation changes such as HorizontalAnimation, or u can extends LinearAnimator and create ur own realisation. 
7) All components of a FlairFramework are linked together by a powerful messaging system. You can notify every part of your system by calling `sendNotification(event, data)` and subscribe on event by calling `registerObserver(event) { INotification -> }` in IMediator or execute another SimpleCommand (see example above). Mediator can notify commands, commands can notify mediators and another commands, proxy can notify mediators and another commands. 

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
    val myProxy by proxyLazy<MyProxy>()
    override fun execute(notification: INotification) {
         myProxy.handleNotification()
         // or you can use inline functions
         // proxy<MyProxy>().handleNotification()
    }
}
```

You can use powerful feature from kotlin lang like lazy `val` instantiating, this is an example with custom constructor parameters. 
```kotlin
class MyProxyWithParams(mediator:MyMediator) : Proxy<MyMediator>(mediator) {
    override fun onRegister() {
        super.onRegister()
        data?.showFuncyMVPHandler()
    }
}

class MyMediator : Mediator() {
  // lazy proxy initialization with params
  val proxy:MyProxyWithParams by proxyLazy(this)
  // or
  // val proxy by proxyLazy<MyProxyWithParams>(this)
  
  fun showFuncyMVPHandler(){
     println("HELLO FROM PROXY TO MEDIATOR")
  }
}
```

Since verson 1.1.3 added new extension functions
* fun IMediator.startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?= null)
* fun IMediator.requestPermissions(permissions: Array<String>, requestCode: Int)
* fun IMediator.checkSelfPermission(permissionToCheck:String):Int
* fun IMediator.shouldShowRequestPermissionRationale(permission: String): Boolean

and they callbacks:
```kotlin
class MyMediator : Mediator() {
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            // do what ever you want with result data
    }
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
          // check if permission granted or not 
    }
}
```

Since version 1.1.4 you can use FlairPagerAdapter for control viewPager with mediators
```kotlin
viewPager?.adapter = FlairPagerAdapter(
                listOf(mediator<PageOneMediator>(), mediator<PageTwoMediator>(), mediator<PageThreeMediator>()),
                listOf("TabOne", "TabTwo", "TabThree"))
tabLayout?.setupWithViewPager(viewPager)
        
        
class PageOneMediator : Mediator() {
// or you can inflate your view from xml by calling inflateView(R.layout.simple_layout)
    override fun createLayout(context: Context): View = with(context) {
        verticalLayout {
            lparams(matchParent, matchParent)

            textView("THIS IS A PAGE ONE MEDIATOR") {
                textSize = 14f
                textColor = Color.GREEN
            }.lparams {
                gravity = Gravity.CENTER_HORIZONTAL
            }
        }
    }
}
```

See the sample project `app` for more complex information. Also code base has good comments and docs on every functions

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

Changelog:
----
* 1.1.8 - Added bundle argument to IMediator, added one more lifecircle fun onPrepareView()
* 1.1.7 - Added hardware back button support (see example in app)
* 1.1.6 - fixed rotation bug with menu creation, many improvements
* 1.1.5 - fix bug in View.kt clearAll()
* 1.1.4 - added com.mincor.flairframework.common.adapters.FlairPagerAdapter
* 1.1.3 - extension functions for permissions and activity

License
----

MIT License

Copyright (c) 2018 Aleksandr Minkin (sphc@yandex.ru)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
