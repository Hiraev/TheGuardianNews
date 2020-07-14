package ru.khiraevmalik.theguardiannews

import org.koin.dsl.module
import ru.khiraevmalik.theguardiannews.api.OkHttpFactory
import ru.khiraevmalik.theguardiannews.api.RetrofitFactory
import ru.khiraevmalik.theguardiannews.main.NewsInteractor
import ru.khiraevmalik.theguardiannews.main.NewsInteractorImpl

val interactors = module {
    single<NewsInteractor> { NewsInteractorImpl(get()) }
}

val dataProvider = module {
    single { RetrofitFactory.createNewsApi(get()) }
    single { RetrofitFactory.createTheGuardianRetrofit(get(), get()) }
    single { OkHttpFactory.createOkHttpClient() }
    single { MoshiFactory.createMoshiForTheGuardianApi() }
    single<NewsRepository> { NewsRepositoryImpl(get()) }
}
