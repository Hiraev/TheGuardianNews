package ru.khiraevmalik.theguardiannews.koin

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import ru.khiraevmalik.theguardiannews.presentation.RootActivityViewModel
import ru.khiraevmalik.theguardiannews.presentation.main.MainNewsViewModel
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.MainReducer
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.MainStore
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.NewsFeedMiddleware
import ru.khiraevmalik.theguardiannews.presentation.main.mvi.SearchMiddleware
import ru.khiraevmalik.theguardiannews.presentation.news_view.NewsViewViewModel
import ru.khiraevmalik.theguardiannews.routing.RootRouter
import ru.terrakok.cicerone.Cicerone

val navigationModule = module {
    single(createdAtStart = true) { RootRouter() }
    single(createdAtStart = true) { Cicerone.create(get<RootRouter>()) }
    single { get<Cicerone<RootRouter>>().navigatorHolder }
}

val viewModelsModule = module {
    viewModel { RootActivityViewModel(get(), get()) }
    viewModel { MainNewsViewModel(get(), get()) }
    viewModel { NewsViewViewModel(get()) }
}

private val MVI_MAIN_MIDDLEWARE = StringQualifier("MVI_MAIN_MIDDLEWARE")

val newsMvi = module {
    factory { SearchMiddleware(get()) }
    factory { NewsFeedMiddleware(get()) }
    factory { MainReducer() }
    factory(MVI_MAIN_MIDDLEWARE) { listOf(get<NewsFeedMiddleware>(), get<SearchMiddleware>()) }
    factory { MainStore(get<MainReducer>(), get(MVI_MAIN_MIDDLEWARE)) }
}
