package impacta.contactless.features.signin.domain

import impacta.contactless.features.signin.data.UserRepository
import impacta.contactless.infra.database.models.User
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(user: User) {
        val savedUser = repository.findById(user.id)
        if (savedUser == null)
            repository.save(user)
    }
}