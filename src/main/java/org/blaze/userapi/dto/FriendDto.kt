package org.blaze.userapi.dto

import org.blaze.userapi.model.F_status
import org.blaze.userapi.model.Profile
import java.time.LocalDateTime
import java.util.*

data class FriendDto(

    val id: Long,
    val sender:Profile,
    val receiver:Profile,
    val status:F_status,
    val createdAt: LocalDateTime,

    )
