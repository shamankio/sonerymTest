package com.rustan.domain.di

import com.rustan.domain.GetDataUseCase
import com.rustan.domain.GetDataUseCaseImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val domainModule = module {
    factory<GetDataUseCase> {
        GetDataUseCaseImpl(
            get(), get(named("dispatcherIO"))
        )
    }
}