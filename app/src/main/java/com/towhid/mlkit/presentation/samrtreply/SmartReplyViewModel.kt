package com.towhid.mlkit.presentation.samrtreply

import androidx.lifecycle.ViewModel
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class SmartReplyViewModel : ViewModel() {

    private val _state = MutableStateFlow(SmartReplyState())
    val state: StateFlow<SmartReplyState> = _state

    private val smartReply = SmartReply.getClient()

    fun onEvent(event: SmartReplyEvent) {
        when (event) {
            is SmartReplyEvent.OnAddMessage -> {
                val newMsg = if (event.isLocalUser)
                    TextMessage.createForLocalUser(event.text, System.currentTimeMillis())
                else
                    TextMessage.createForRemoteUser(event.text, System.currentTimeMillis(), "user1")

                _state.update {
                    it.copy(conversation = it.conversation + newMsg)
                }
            }

            SmartReplyEvent.OnGenerateReplies -> {
                _state.update { it.copy(isLoading = true, error = null) }

                smartReply.suggestReplies(_state.value.conversation)
                    .addOnSuccessListener { result ->
                        if (result.status == SmartReplySuggestionResult.STATUS_SUCCESS) {
                            _state.update {
                                it.copy(
                                    suggestions = result.suggestions.map { it.text },
                                    isLoading = false
                                )
                            }
                        }
                    }
                    .addOnFailureListener {
                        _state.update { it.copy(error = it.error, isLoading = false) }
                    }
            }

            SmartReplyEvent.OnReset -> {
                _state.value = SmartReplyState()
            }
        }
    }
}

data class SmartReplyState(
    val conversation: List<TextMessage> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
sealed class SmartReplyEvent {
    data class OnAddMessage(val text: String, val isLocalUser: Boolean) : SmartReplyEvent()
    object OnGenerateReplies : SmartReplyEvent()
    object OnReset : SmartReplyEvent()
}

