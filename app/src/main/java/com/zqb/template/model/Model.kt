package com.zqb.template.model

import com.zqb.template.repository.Repository
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel = module {
    single { Repository() }
    viewModel { MainModel(get()) }
}