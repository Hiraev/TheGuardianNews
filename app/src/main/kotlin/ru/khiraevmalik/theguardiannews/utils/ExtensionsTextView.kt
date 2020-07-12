package ru.khiraevmalik.theguardiannews.utils

import android.text.TextWatcher
import android.widget.TextView

fun TextView.addOnTextChangedListener(listener: (String) -> Unit): TextWatcher {
    val textWatcher = OnTextChangedListener(listener)
    addTextChangedListener(textWatcher)
    return textWatcher
}
