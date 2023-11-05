package impacta.contactless.features.activekeys.domain

import android.util.Patterns

class ValidateURLUseCase {
    operator fun invoke(target: CharSequence?): Boolean {
        return if (target == null) {
            false
        } else {
            return target.last() == '/' &&
            Patterns.WEB_URL.matcher(target).matches()
        }
    }
}