package org.blaze.userapi.dto

import org.blaze.userapi.model.F_status
import org.blaze.userapi.model.Profile
import java.time.LocalDateTime
import java.util.*

data class FriendDto(
    val id: UUID?,
    val senderId: Profile,
    val receiverId: Profile,
    val status: F_status,
    val createdAt: LocalDateTime
)
