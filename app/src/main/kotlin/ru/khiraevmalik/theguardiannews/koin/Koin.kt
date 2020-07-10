package ru.khiraevmalik.theguardiannews.koin

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.khiraevmalik.theguardiannews.presentation.RootActivityViewModel
import ru.khiraevmalik.theguardiannews.routing.RootRouter
import ru.terrakok.cicerone.Cicerone

val navigationModule = module {
    single(createdAtStart = true) { RootRouter() }
    single(createdAtStart = true) { Cicerone.create(get<RootRouter>()) }
    single { get<Cicerone<RootRouter>>().navigatorHolder }
}

val viewModelsModule = module {
    viewModel { RootActivityViewModel(get(), get()) }
}
