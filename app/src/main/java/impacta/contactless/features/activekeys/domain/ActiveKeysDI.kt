package impacta.contactless.features.activekeys.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import impacta.contactless.features.activekeys.data.KeysRepository
import impacta.contactless.features.activekeys.data.KeysRepositoryImpl
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class ActiveKeysDI {

    @Provides
    @ViewModelScoped
    fun keysRepository(retrofit: Retrofit): KeysRepository = KeysRepositoryImpl(retrofit)

    @Provides
    fun getKeysForUserUC(repository: KeysRepository): GetKeyForUserUseCase = GetKeyForUserUseCase(repository)

}