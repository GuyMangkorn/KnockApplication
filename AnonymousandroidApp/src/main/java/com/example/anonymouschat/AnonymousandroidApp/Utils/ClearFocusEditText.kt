package com.example.anonymouschat.AnonymousandroidApp.Utils

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import com.google.android.material.textfield.TextInputEditText

class ClearFocusEditText : TextInputEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            clearFocus()
        }else if(event!!.action ==  MotionEvent.ACTION_UP){
            hint = "กายซ่า"
        }
        return super.onKeyPreIme(keyCode, event)
    }
}