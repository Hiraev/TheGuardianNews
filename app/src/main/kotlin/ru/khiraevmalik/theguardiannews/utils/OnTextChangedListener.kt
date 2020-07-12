package ru.khiraevmalik.theguardiannews.utils

import android.text.Editable
import android.text.TextWatcher

class OnTextChangedListener(private val listener: (String) -> Unit) : TextWatcher {

    override fun afterTextChanged(s: Editable) {
        listener.invoke(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

}
