package com.nyan.photokmm.di

import com.nyan.photokmm.data.remote.RemoteDataSource
import com.nyan.photokmm.data.remote.repository.PhotoRepositoryImpl
import com.nyan.photokmm.data.remote.service.PhotoService
import com.nyan.photokmm.data.utils.provideDispatcher
import com.nyan.photokmm.domain.repository.PhotoRepository
import com.nyan.photokmm.domain.usecase.GetPhotosUseCase
import org.koin.dsl.module

private val dataModule = module {
    factory { RemoteDataSource(get(), get()) }
    factory { PhotoService() }
}

private val utilityModule = module {
    factory { provideDispatcher() }
}

private val domainModule = module {
    single<PhotoRepository> { PhotoRepositoryImpl(get()) }
    factory { GetPhotosUseCase() }
}

private val sharedModules = listOf(domainModule, dataModule, utilityModule)

fun getSharedModules() = sharedModules