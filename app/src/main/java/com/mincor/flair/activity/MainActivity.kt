package com.mincor.flair.activity

import android.os.Bundle
import com.mincor.flair.views.MVVMMediator
import com.mincor.flairframework.core.FlairActivity
import com.mincor.flairframework.core.animation.LinearAnimator
import com.mincor.flairframework.ext.flair
import com.mincor.flairframework.interfaces.handleBackButton
import com.mincor.flairframework.interfaces.showLastOrExistMediator

class MainActivity : FlairActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flair().attach(this).showLastOrExistMediator<MVVMMediator>()
    }

    override fun onBackPressed() {
        if(!flair().handleBackButton()) {
            super.onBackPressed()
        }
    }
}
