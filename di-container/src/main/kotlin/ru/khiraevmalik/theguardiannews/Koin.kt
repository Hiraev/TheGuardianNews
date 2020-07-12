package ru.khiraevmalik.theguardiannews

import org.koin.dsl.module
import ru.khiraevmalik.theguardiannews.main.NewsInteractor
import ru.khiraevmalik.theguardiannews.main.NewsInteractorImpl

val interactors = module {
    single<NewsInteractor> { NewsInteractorImpl() }
}
