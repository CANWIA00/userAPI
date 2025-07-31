package org.blaze.userapi.dto.request

import org.blaze.userapi.model.SignalType

data class SignalMessageRequest(
    val from: String,
    val to: String,
    val type: SignalType,
    val sdp: String? = null
)
