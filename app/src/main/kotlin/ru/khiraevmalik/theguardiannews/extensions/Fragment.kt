package ru.khiraevmalik.theguardiannews.extensions

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment

inline fun <reified T : Parcelable> Fragment.args(key: String) = lazy<T> {
    arguments?.getParcelable(key) ?: throw Throwable("Can't extract ${T::class.qualifiedName} from arguments in ${this::class.qualifiedName}")
}

fun <T : Fragment> T.withParcelable(key: String, parcelable: Parcelable): T = apply {
    arguments = Bundle().apply {
        putParcelable(key, parcelable)
    }
}
