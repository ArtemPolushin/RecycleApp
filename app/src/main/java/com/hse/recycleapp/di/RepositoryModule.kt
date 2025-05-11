package com.hse.recycleapp.di
import com.google.firebase.firestore.FirebaseFirestore
import com.hse.recycleapp.data.repository.RecyclePointRepository
import com.hse.recycleapp.domain.repository.IRecyclePointRepository
import com.hse.recycleapp.data.repository.RecyclePointRepositoryLocal
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideRecyclePointRepository(
        firestore: FirebaseFirestore
    ): IRecyclePointRepository = RecyclePointRepository(firestore)

//    @Provides
//    @Singleton
//    fun provideRecyclePointRepository(): IRecyclePointRepository {
//        return RecyclePointRepositoryLocal()
//    }
}
