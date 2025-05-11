package com.hse.recycleapp.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hse.recycleapp.data.repository.AuthRepository
import com.hse.recycleapp.domain.repository.IAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {
    @Provides
    fun provideAuthRepository(): IAuthRepository = AuthRepository(
        auth = Firebase.auth
    )
}