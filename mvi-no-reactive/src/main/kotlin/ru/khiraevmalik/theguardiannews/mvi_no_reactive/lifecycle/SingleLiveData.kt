package ru.khiraevmalik.theguardiannews.mvi_no_reactive.lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Sends events only one time and only when there is at least one active observer
 * If there are no any active observers an event will be lost
 */
class SingleLiveData<T> : MutableLiveData<T>() {

    private var pending = AtomicBoolean(false)

    override fun setValue(value: T) {
        if (hasActiveObservers()) {
            pending.set(true)
            super.setValue(value)
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer { value ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(value)
            }
        })
    }

}