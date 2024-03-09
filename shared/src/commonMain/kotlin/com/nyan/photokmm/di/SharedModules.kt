package com.nyan.photokmm.di

import com.nyan.photokmm.data.remote.RemoteDataSource
import com.nyan.photokmm.data.remote.repository.PhotoRepositoryImpl
import com.nyan.photokmm.data.remote.service.PhotoService
import com.nyan.photokmm.data.utils.provideDispatcher
import com.nyan.photokmm.data.utils.provideFileSystemAccess
import com.nyan.photokmm.domain.repository.PhotoRepository
import com.nyan.photokmm.domain.usecase.DownloadImageUseCase
import com.nyan.photokmm.domain.usecase.GetPhotosUseCase
import org.koin.dsl.module

private val utilityModule = module {
    factory { provideDispatcher() }
    factory { provideFileSystemAccess() }
}

private val dataModule = module {
    factory { PhotoService() }
    factory { RemoteDataSource(get(), get()) }
}

private val domainModule = module {
    single<PhotoRepository> { PhotoRepositoryImpl(get()) }
    factory { GetPhotosUseCase() }
    factory { DownloadImageUseCase() }
}

private val sharedModules = listOf(utilityModule, dataModule, domainModule)

fun getSharedModules() = sharedModules