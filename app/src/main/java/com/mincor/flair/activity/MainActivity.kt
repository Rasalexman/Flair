package com.mincor.flair.activity

import android.os.Bundle
import android.util.Log
import com.mincor.flair.BuildConfig
import com.mincor.flair.views.MVVMMediator
import com.mincor.flairframework.core.FlairActivity
import com.mincor.flairframework.core.animation.LinearAnimator
import com.mincor.flairframework.ext.flair
import com.mincor.flairframework.interfaces.showLastOrExistMediator

class MainActivity : FlairActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flair().attach(this).showLastOrExistMediator<MVVMMediator>(LinearAnimator())
    }
}

inline fun log(lambda: () -> String) {
    if (BuildConfig.DEBUG) {
        Log.d("KOTLIN_TAG", lambda())
    }
}
