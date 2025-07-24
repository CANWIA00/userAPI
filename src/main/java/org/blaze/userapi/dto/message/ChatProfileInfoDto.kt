package org.blaze.userapi.dto.message

import org.blaze.userapi.model.Status
import java.time.Instant
import java.util.*

data class ChatProfileInfoDto(

    val senderFullName: String,

    val senderUserMail: String,

    val senderUserId: UUID,

    val receiverFullName: String,

    val receiverUserMail: String,

    val receiverUserId: UUID,

)
