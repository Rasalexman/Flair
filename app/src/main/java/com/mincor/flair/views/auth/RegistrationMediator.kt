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
import com.rasalexman.flairframework.core.animation.LinearAnimator
import com.rasalexman.flairframework.ext.log
import com.rasalexman.flairframework.interfaces.*
import com.rasalexman.flairframework.patterns.mediator.Mediator
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by Alex on 22.01.2017.
 */

class RegistrationMediator : Mediator() {
    override fun createLayout(context: Context): View = RegisterUI().createView(AnkoContext.create(context, this))

    private var nameET: EditText? = null
    private var emailET: EditText? = null
    private var passwordET: EditText? = null
    private var repeatePasswordET: EditText? = null

    internal fun registerUser() {
        log { "START LOGIN IN " }

        // Reset errors.
        nameET!!.error = null
        emailET!!.error = null
        passwordET!!.error = null
        repeatePasswordET!!.error = null

        val nameStr = nameET!!.text.toString()
        val emailStr = emailET!!.text.toString()
        val passStr = passwordET!!.text.toString()
        val repeatePassStr = repeatePasswordET!!.text.toString()

        var cancel = false
        var focusView: View? = null

        if (nameStr.isEmpty() || nameStr.length == 1) {
            nameET!!.error = string(R.string.notSetNameTF)
            focusView = nameET
            cancel = true
        } else if (emailStr.isEmpty()) {
            emailET!!.error = string(R.string.notSetEmailTF)
            focusView = emailET
            cancel = true
        } else if (passStr.isEmpty()) {       // Check for a valid password, if the user entered one.
            passwordET!!.error = string(R.string.notSetPassTF)
            focusView = passwordET
            cancel = true
        } else if (passStr.length < 5) {       // Check for a valid password, if the user entered one.
            passwordET!!.error = "${string(R.string.minPassErrorTF)} 5"
            focusView = passwordET
            cancel = true
        } else if (passStr != repeatePassStr) {  // Check for a valid password, if the user entered one.
            repeatePasswordET!!.error = string(R.string.notEqualPassTF)
            focusView = repeatePasswordET
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {

        }
    }

    private inner class RegisterUI : AnkoComponent<RegistrationMediator> {
        override fun createView(ui: AnkoContext<RegistrationMediator>): View = with(ui) {
            verticalLayout {
                background = gradientBg(arrayOf(color(R.color.startColor), color(R.color.endColor)))
                gravity = Gravity.CENTER

                nameET = editText {
                    background = roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14
                    textColor = color(R.color.colorPrimaryText)
                    hint = string(R.string.nameTF)
                    setPadding(dip8(), dip8(), dip8(), dip8())
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                }.lparams(wdthProc(0.6f))

                emailET = editText {
                    background = roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14
                    textColor = color(R.color.colorPrimaryText)
                    hint = string(R.string.emailTF)
                    setPadding(dip8(), dip8(), dip8(), dip8())
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                }.lparams(wdthProc(0.6f)) {
                    topMargin = dip8()
                }

                passwordET = editText {
                    background = roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14
                    textColor = color(R.color.colorPrimaryText)
                    hint = string(R.string.passTF)
                    setPadding(dip8(), dip8(), dip8(), dip8())
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }.lparams(wdthProc(0.6f)) {
                    topMargin = dip8()
                }

                repeatePasswordET = editText {
                    background = roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14
                    textColor = color(R.color.colorPrimaryText)
                    hint = string(R.string.passRepeatTF)
                    setPadding(dip8(), dip8(), dip8(), dip8())
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }.lparams(wdthProc(0.6f)) {
                    topMargin = dip8()
                }

                button(R.string.submitTF) {
                    background = roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14
                    onClick {
                        registerUser()
                    }
                }.lparams(wdthProc(0.6f), hdthProc(0.06f)) {
                    topMargin = dip8()
                }

                button(R.string.backTF) {
                    background = roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_12
                    textColor = color(R.color.colorPrimaryText)

                    onClick {
                        popToBack(LinearAnimator())
                    }
                }.lparams(wdthProc(0.4f), hdthProc(0.06f)) {
                    topMargin = dip8()
                }
            }
        }
    }
    /*internal var name: EditText? = null
    internal var email: EditText? = null
    internal var password: EditText? = null

    internal var user_avatar: CircleImageView? = null

    @BindString(R.string.error_not_set_avatar)
    internal var error_not_set_avatar: String by stringRes(R.string.error_not_set_avatar)
    @BindString(R.string.warningTF)
    internal var warningTF: String? = null
    @BindString(R.string.loadingTF)
    internal var loadingTF: String? = null
    @BindString(R.string.accCreationErrorTF)
    internal var accCreationErrorTF: String? = null
    @BindString(R.string.accCreationErrorMessTF)
    internal var accCreationErrorMessTF: String? = null
    @BindString(R.string.accCreateSuccTF)
    internal var accCreateSuccTF: String? = null
    @BindString(R.string.accCreateSuccMessTF)
    internal var accCreateSuccMessTF: String? = null

    private var bmp: Bitmap? = null*/

    /*protected fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_registration, container, false)
    }*/

    /*protected fun onDetach(view: View) {
        super.onDetach(view)
        user_avatar!!.setImageBitmap(null)
        user_avatar!!.setImageResource(0)
        user_avatar!!.setImageDrawable(null)
    }

    @OnClick(R.id.profile_image_register)
    internal fun selectAvatar() {
        val i = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, 1)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data

            if (bmp != null) {
                bmp!!.recycle()
                bmp = null
            }

            try {
                bmp = Support.getBitmapFromUri(selectedImage, this.getActivity())
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            user_avatar!!.setImageBitmap(bmp)
        } else {
            user_avatar!!.setImageResource(R.drawable.default_avatar)
        }
    }

    @OnClick(R.id.button3)
    internal fun registerUser() {
        L.d("START LOGIN IN ")

        // Reset errors.
        name!!.error = null
        email!!.error = null
        password!!.error = null

        val nameStr = name!!.text.toString()
        val emailStr = email!!.text.toString()
        val passStr = password!!.text.toString()

        var cancel = false
        var focusView: View? = null

        if (TextUtils.isEmpty(nameStr) || nameStr.length == 1) {
            name!!.error = getActivity().getString(R.string.notSetNameTF)
            focusView = name
            cancel = true
        } else if (!Support.isValidEmail(emailStr) || TextUtils.isEmpty(emailStr)) {
            email!!.error = getActivity().getString(R.string.notSetEmailTF)
            focusView = email
            cancel = true
        } else if (TextUtils.isEmpty(passStr)) {       // Check for a valid password, if the user entered one.
            password!!.error = getActivity().getString(R.string.notSetPassTF)
            focusView = password
            cancel = true
        } else if (!Support.isPasswordValid(passStr)) {  // Check for a valid password, if the user entered one.
            password!!.error = getActivity().getString(R.string.minPassErrorTF) + " " + Constants.PASSWORD_MIN_LENGTH
            focusView = password
            cancel = true
        } else if (bmp == null) {
            cancel = true
            Support.alertDisplayerWithOK(this.getActivity(), warningTF, error_not_set_avatar, null)
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {

            this.getView().setEnabled(false)
            Support.showHideProgressDialog(true, this.getActivity(), loadingTF)

            val user = ParseUser()
            user.email = emailStr
            user.username = emailStr
            user.setPassword(passStr)
            user.put(Constants.USER_NAME, nameStr)
            user.put("isPremium", false)
            user.put("askieCoins", 0)

            // проверяем аватар если он есть сначала сохраняем его
            if (bmp != null) {
                val data = Support.getByteArrayfromBitmap(bmp)
                val file = ParseFile("avatar.png", data)
                file.saveInBackground(SaveCallback { e ->
                    if (e == null) {
                        user.put("avatar", file)
                        user.signUpInBackground { e1 ->
                            if (e1 == null) {
                                successfullCreation()
                            } else {
                                showErrorCreateUser()
                            }
                        }
                    } else {
                        showErrorCreateUser()
                    }
                })
            } else {
                user.signUpInBackground { e ->
                    if (e == null) {
                        successfullCreation()
                    } else {
                        showErrorCreateUser()
                    }
                }
            }
        }
    }

    private fun showErrorCreateUser() {
        this.getView().setEnabled(true)
        Support.showHideProgressDialog(false, null, null)
        Support.alertDisplayerWithOK(this.getActivity(), accCreationErrorTF, accCreationErrorMessTF, null)
    }

    private fun successfullCreation() {
        Support.showHideProgressDialog(false, null, null)
        Support.alertDisplayerWithOK(getActivity(), accCreateSuccTF, accCreateSuccMessTF,
                { dialog, which ->
                    dialog.cancel()
                    showSingInHandler()
                    //Support.showDefaultMailProgramm(getActivity());
                })
    }

    @OnClick(R.id.textView)
    internal fun showSingInHandler() {
        this.getRouter().popCurrentController()
    }*/
}
