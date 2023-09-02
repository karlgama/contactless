package impacta.contactless.features.activekeys.domain

import impacta.contactless.features.activekeys.data.KeysRepository
import impacta.contactless.infra.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetKeyForUserUseCase @Inject constructor(
    private val repository: KeysRepository
) {
    operator fun invoke(userId: String): Flow<Result<String>> = flow {
        emit(Result.Loading)
        try {
            emit(Result.Success(repository.getKeyForUser(userId)))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }
}