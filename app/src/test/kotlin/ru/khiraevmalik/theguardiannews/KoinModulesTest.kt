package ru.khiraevmalik.theguardiannews

import org.junit.Test
import org.koin.test.check.checkModules
import ru.khiraevmalik.theguardiannews.koin.navigationModule
import ru.khiraevmalik.theguardiannews.koin.viewModelsModule

class KoinModulesTest {

    @Test
    fun `check Koin modules test`() {
        checkModules {
            modules(navigationModule, viewModelsModule, interactors)
        }
    }

}
