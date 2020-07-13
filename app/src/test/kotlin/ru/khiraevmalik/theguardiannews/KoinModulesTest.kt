package ru.khiraevmalik.theguardiannews

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import org.junit.BeforeClass
import org.junit.Test
import org.koin.test.check.checkModules
import ru.khiraevmalik.theguardiannews.koin.navigationModule
import ru.khiraevmalik.theguardiannews.koin.newsMvi
import ru.khiraevmalik.theguardiannews.koin.viewModelsModule

class KoinModulesTest {

    companion object {
        private val mainThreadSurrogate = newSingleThreadContext("UI thread")

        @BeforeClass
        @JvmStatic
        fun setUp() {
            Dispatchers.setMain(mainThreadSurrogate)
        }
    }

    @Test
    fun `check Koin modules test`() {
        checkModules {
            modules(navigationModule, viewModelsModule, interactors, newsMvi)
        }
    }

}
