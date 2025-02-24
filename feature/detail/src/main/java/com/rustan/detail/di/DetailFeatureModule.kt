package com.rustan.detail.di

import com.rustan.detail.DetailScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val detailFeatureModule = module {
    viewModel {
        DetailScreenViewModel()
    }
}