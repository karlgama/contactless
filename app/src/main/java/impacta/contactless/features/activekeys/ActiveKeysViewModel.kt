package impacta.contactless.features.activekeys

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import impacta.contactless.features.activekeys.domain.GetKeyForUserUseCase
import impacta.contactless.infra.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
sealed interface ActiveKeysUIState {
    data class Success(val code: String?) : ActiveKeysUIState
    object Error : ActiveKeysUIState
    object Loading : ActiveKeysUIState
}

data class ActiveKeysScreenUIState(
    val activeKeysUIState: ActiveKeysUIState
)

@HiltViewModel
class ActiveKeysViewModel @Inject constructor(
    var getKeyForUserUseCase: GetKeyForUserUseCase
) : ViewModel() {
    private val _key = MutableStateFlow(ActiveKeysScreenUIState(ActiveKeysUIState.Loading))
    val key = _key.asStateFlow()

    fun getKey(user: String) = viewModelScope.launch(Dispatchers.IO) {
        getKeyForUserUseCase(user).collect { result ->
            val activeKeysUIState = when (result) {
                is Result.Success -> ActiveKeysUIState.Success(result.data)
                is Result.Loading -> ActiveKeysUIState.Loading
                is Result.Failure -> ActiveKeysUIState.Error
            }
            _key.value = ActiveKeysScreenUIState(activeKeysUIState)
        }
    }
}
