package com.nyan.photokmm.android.di

import com.nyan.photokmm.android.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { DashboardViewModel(get(), get()) }
}