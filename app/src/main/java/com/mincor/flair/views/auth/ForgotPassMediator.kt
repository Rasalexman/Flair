package com.mincor.flair.views.auth

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.EditText
import com.mincor.flair.R
import com.mincor.flair.utils.Consts.FONT_SIZE_12
import com.mincor.flair.utils.Consts.FONT_SIZE_14
import com.mincor.flair.utils.Consts.ROUND_CORNERS_16
import com.mincor.flair.utils.dip8
import com.rasalexman.flaircore.animation.LinearAnimator
import com.rasalexman.flaircore.ext.*
import com.rasalexman.flaircore.patterns.mediator.Mediator
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by Alex on 22.01.2017.
 */

class ForgotPassMediator : Mediator() {
    override fun createLayout(context: Context): View = ForgotUI().createView(AnkoContext.create(context, this))

    internal var emailET: EditText? = null

    internal fun onSubmitHandler() {
        val emailStr = emailET!!.text.toString()
        if (emailStr.isEmpty()) {
            emailET?.requestFocus()
        }
    }

    private class ForgotUI : AnkoComponent<ForgotPassMediator> {
        override fun createView(ui: AnkoContext<ForgotPassMediator>): View = with(ui) {
            verticalLayout {
                background = gradientBg(arrayOf(ui.owner.color(R.color.startColor), ui.owner.color(R.color.endColor)))
                gravity = Gravity.CENTER

                ui.owner.emailET = editText {
                    background = ui.owner.roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14
                    textColor = ui.owner.color(R.color.colorPrimaryText)
                    hint = ui.owner.string(R.string.emailTF)
                    setPadding(dip8(), dip8(), dip8(), dip8())
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                }.lparams(ui.owner.wdthProc(0.6f)) {
                    topMargin = dip8()
                }

                button(R.string.submitTF) {
                    background = ui.owner.roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14

                    onClick {
                        ui.owner.onSubmitHandler()
                    }
                }.lparams(ui.owner.wdthProc(0.6f), ui.owner.hdthProc(0.06f)) {
                    topMargin = dip8()
                }

                button(R.string.backTF) {
                    background = ui.owner.roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_12
                    textColor = ui.owner.color(R.color.colorPrimaryText)

                    onClick {
                        ui.owner.popToBack(LinearAnimator())
                    }
                }.lparams(ui.owner.wdthProc(0.4f), ui.owner.hdthProc(0.06f)) {
                    topMargin = dip8()
                }
            }
        }
    }
}
