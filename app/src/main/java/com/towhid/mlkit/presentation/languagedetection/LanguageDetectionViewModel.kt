package com.towhid.mlkit.presentation.languagedetection

import androidx.lifecycle.ViewModel
import com.google.mlkit.nl.languageid.LanguageIdentification
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale


class LanguageIdViewModel : ViewModel() {

    private val _state = MutableStateFlow(LanguageIdState())
    val state: StateFlow<LanguageIdState> = _state

    private val languageIdentifier = LanguageIdentification.getClient()

    fun onEvent(event: LanguageIdEvent) {
        when (event) {
            is LanguageIdEvent.OnTextChange -> {
                _state.update { it.copy(inputText = event.text) }
            }

            LanguageIdEvent.OnIdentifyLanguage -> {
                val input = _state.value.inputText
                if (input.isBlank()) return

                _state.update { it.copy(isLoading = true, error = null) }

                languageIdentifier.identifyLanguage(input)
                    .addOnSuccessListener { langCode ->
                        val locale = Locale.forLanguageTag(langCode)
                        val languageName = locale.getDisplayLanguage(Locale.ENGLISH)
                        _state.update { it.copy(languageCode = languageName, isLoading = false) }
                    }
                    .addOnFailureListener {
                        _state.update { it.copy(error = it.error, isLoading = false) }
                    }
            }

            LanguageIdEvent.OnReset -> {
                _state.value = LanguageIdState()
            }
        }
    }
}

data class LanguageIdState(
    val inputText: String = "",
    val languageCode: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class LanguageIdEvent {
    data class OnTextChange(val text: String) : LanguageIdEvent()
    object OnIdentifyLanguage : LanguageIdEvent()
    object OnReset : LanguageIdEvent()
}

