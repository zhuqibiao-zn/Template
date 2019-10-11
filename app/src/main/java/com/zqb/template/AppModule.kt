package com.zqb.template

import org.koin.dsl.module

val executorsModule = module {
    single { AppExecutors() }
}