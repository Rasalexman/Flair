package com.mincor.flair.activity

import android.os.Bundle
import com.mincor.flair.views.MVVMMediator
import com.rasalexman.flairframework.core.FlairActivity
import com.rasalexman.flairframework.ext.flair
import com.rasalexman.flairframework.interfaces.handleBackButton
import com.rasalexman.flairframework.interfaces.showLastOrExistMediator

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
