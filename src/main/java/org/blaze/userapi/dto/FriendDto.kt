package org.blaze.userapi.dto

import org.blaze.userapi.model.F_status
import org.blaze.userapi.model.Friend
import org.blaze.userapi.model.Profile
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

data class FriendDto(
    val id: UUID?,
    val sender: Profile,
    val receiver: Profile,
    val status: F_status,
    val createdAt: LocalDateTime
): Serializable{
    constructor() : this (
        id = null,
        sender = Profile(),
        receiver = Profile(),
        status = F_status.PENDING,
        createdAt = LocalDateTime.now()
    )
}