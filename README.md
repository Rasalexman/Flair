# FlairFramework

![alt text](https://github.com/Rasalexman/Flair/blob/master/FlairFrameworkLogo.png)

[ ![Kotlin 1.3.11](https://img.shields.io/badge/Kotlin-1.3.11-blue.svg)](http://kotlinlang.org) [ ![Download](https://api.bintray.com/packages/sphc/FlairFramework/flaircore/images/download.svg) ](https://bintray.com/sphc/FlairFramework/flaircore/_latestVersion)[![Codacy Badge](https://api.codacy.com/project/badge/Grade/f3452befd9544f65aa5b93fdefd3cd38)](https://app.codacy.com/app/Rasalexman/Flair?utm_source=github.com&utm_medium=referral&utm_content=Rasalexman/Flair&utm_campaign=Badge_Grade_Dashboard)[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)

This is an android framework for build complex application with different architectures (MVC ready/MVP/MVVM/MVI ets). It's create on top of MVC pattern with powerful event system, constructor injection module and property delegation, also it support multi-core instances and animation changes between views (see example project for more information). 
The `FlairFramework` is easy to use, it's light-weight, extensible, flexible and it's has more simplier view lifecycle than Fragments and Activities

The start point for initialize framework is declare 'flair' instance in onCreate method in MainApplication file. But u can initialize framework in any part of your project such as `MainActivity` or any `Context` implementations
```kotlin
val flairCoreInstance = flair {
        registerCommand<MyCommand>(eventName) { MyCommand() }
        registerProxy<MyProxy> { MyProxy() }
        registerMediator<MyMediator> { MyMediator() }
    }
    // you can define more than one core instance of flair by given the name
val flairSecondCore = flair(SECOND_CORE_NAME) {}
```

The second point or using 'Flair' is attach created core to single Activity class and root layout container (but u can no specify any root container and 'Flair' take it for you automatically as `activity.window.decorView.findViewById(android.R.id.content)`). Important thing: only one activity can be stored in one core of FlairFramework
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
2) SimpleCommand instances is a command pattern realisation. You can manipulate proxy objects from it's instance as like usecases by siquence one command to notification like `registerCommand<MyCommand>(YOUR_EVENT_NAME) { MyCommand() }`
3) MacroCommands can combine more than one SimpleCommand and execute it one by one
4) Proxy objects is a complex object that store data to manipulate with, it's like repository for ur network calls or database
5) Mediator is a simple view-hierarchy handler class, it's store and manage life cycle of your view components such as AnkoComponents or xml-layout files. Also it support view backstack storage.
6) Also you has `LinearAnimator.kt` for create simple view animation changes such as HorizontalAnimation, or u can extends LinearAnimator and create your own realisation. 
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
  override fun createLayout(context: Context): View = UserAuthUI().createView(AnkoContext.create(context, this)) 
  // or you can inflate you custom xml layout
  // override fun createLayout(context: Context): View = inflateView(R.layout.simple_layout) 
  
  inner class UserAuthUI : AnkoComponent<MyMediator> {
    override fun createView(ui: AnkoContext<MyMediator>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            textView("HELLO WORLD")
        }
    }
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

Register all components of Flair framework (Mediators, Proxies, Command) in any part of your application by calling lazy functions or inline functions like `proxy()`, `proxyLazy()`, `mediator()`, `mediatorLazy()` from reflection module since 1.5.+ 

You can use powerful feature from kotlin lang like lazy `val` instantiating, this is an example with custom constructor parameters. Important note: that since version 1.5.+ you need to add  
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
```kotlin
fun IMediator.startActivityForResult(intent: Intent, requestCode: Int, options: Bundle?= null)
fun IMediator.requestPermissions(permissions: Array<String>, requestCode: Int)
fun IMediator.checkSelfPermission(permissionToCheck:String):Int
fun IMediator.shouldShowRequestPermissionRationale(permission: String): Boolean
```
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
Since version 1.5.0 - there are many new features and changes in framework:
* The core version is under `com.rasalexman.flaircore` package and you need to add new package`implementation 'com.rasalexman.flaircore:flaircore:1.5.+'` into your build.gradle file
* The reflection module included by implementing `com.rasalexman.flairreflect:flairreflect:1.5.x` and has all the reflection library features like constructor injection, lazy initialization and all the features that was at pre 1.5.+ (1.x.y).
* Added new animations `FadeAnimator`, `NextLinearAnimator`, `BackLinearAnimator`.
* Turned back minSdkVersion = 17 ) 

See the sample project `app` for more complex information. Also code base has good comments and docs on every functions

Maven:
```kotlin
// Core
<dependency>
  <groupId>com.rasalexman.flaircore</groupId>
  <artifactId>flaircore</artifactId>
  <version>1.5.x</version>
  <type>pom</type>
</dependency>

// reflection module
<dependency>
  <groupId>com.rasalexman.flairreflect</groupId>
  <artifactId>flairreflect</artifactId>
  <version>1.5.x</version>
  <type>pom</type>
</dependency>
```

Gradle:
```kotlin
// standart multicore version (without reflection)
implementation 'com.rasalexman.flaircore:flaircore:1.5.x'

// reflection module (for use constructor injections and property injection)
implementation 'com.rasalexman.flairreflect:flairreflect:1.5.x'
```

Changelog
----
* 1.5.1 - Added AppCompatActivity to `View.attachActivity(...)` with activity fragmentManager.

* 1.5.0
1) Separate FlairFramework packages to core and reflection modules. Now core module weight is less then 125 Kb and you don't need to worry about reflection library in your proguard file!!!
2) Add example with GOOGLE LiveData
3) minSdkVersion come back to 17
4) IMediator.isAddToBackStack - new property that means: `does this mediator need to be added in backstack` if you want to organize your own backstack)
5) Added new animations - FadeAnimator, NextLinearAnimator, BackLinearAnimator.
6) Many bug fixes and code improvements

* 1.2.5
1) fix bug with IView.checkSelfPermission(permissionToCheck: String)
2) update kotlin version to 1.2.70

* 1.2.4
1) added some useful extension functions
2) Model is final class now

* 1.2.3:
1) fixed bug in IView.hideMediator when pop curent Mediator after animation changed
2) added hashBackButton:Boolean to ToolbarMediator
3) minSdkVersion 19
4) split inner classes from `com.rasalexman.flairframework.core.animation.*` to AnimationPreDrawListener, BaseAnimationListenerAdapter and added abstract class BaseAnimator
5) changed MutableMap to ArrayMap for memory improvements
6) View.currentActivity is WeakReference
7) changed MacroCommand.initializeMacroCommand from constructor to IController.registerCommand
8) added IMediator.onAnimationStart and IMediator.onAnimationFinish
9) added IMediator.removeMediator
10) Docs added, plus a lot of memory improvements and code refactoring. 

* 1.2.0:
1) Change `flair` package name to `com.rasalexman.flairframework`
2) Added IMediator.startActivity(intent:Intend, bundle:Bundle? = null) for start another activity from mediators
* 1.1.9 - added IMediator.removeObserver and IMediator.removeAllObservers to manually remove notification observers from mediator instance
* 1.1.8 - Added bundle argument to IMediator, added one more lifecyrcle fun onPrepareView()
* 1.1.7 - Added hardware back button support (see example in app)
* 1.1.6 - fixed rotation bug with menu creation, many improvements
* 1.1.5 - fix bug in View.kt `clearAll()`
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
