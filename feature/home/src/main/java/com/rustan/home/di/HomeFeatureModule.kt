package com.rustan.home.di

import com.rustan.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeFeatureModule = module {
    viewModel {
        HomeViewModel(get())
    }
}