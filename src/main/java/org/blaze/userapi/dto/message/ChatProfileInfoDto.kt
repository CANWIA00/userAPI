package org.blaze.userapi.dto.message

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ChatProfileInfoDto(

    val senderFullName: String,

    val senderUserMail: String,

    val senderPhotoUrl: String?,

    val senderUserId: UUID,

    val receiverFullName: String,

    val receiverUserMail: String,

    val receiverPhotoUrl: String?,

    val receiverUserId: UUID,

)
