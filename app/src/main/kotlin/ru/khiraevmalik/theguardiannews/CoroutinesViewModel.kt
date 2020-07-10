package ru.khiraevmalik.theguardiannews

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class CoroutinesViewModel : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.Main + Job() + CoroutineName(javaClass.name))

    override fun onCleared() {
        super.onCleared()
        scope.cancel("ViewModel is cleared")
    }

    protected fun launch(
            context: CoroutineContext = EmptyCoroutineContext,
            start: CoroutineStart = CoroutineStart.DEFAULT,
            block: suspend CoroutineScope.() -> Unit
    ) = scope.launch(context, start, block)

    protected fun <T> async(
            context: CoroutineContext = EmptyCoroutineContext,
            start: CoroutineStart = CoroutineStart.DEFAULT,
            block: suspend CoroutineScope.() -> T
    ) = scope.async(context, start, block)

}
