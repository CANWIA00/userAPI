package org.blaze.userapi.dto

import org.blaze.userapi.model.F_status
import org.blaze.userapi.model.Friend
import org.blaze.userapi.model.Profile
import org.blaze.userapi.model.Status
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

data class FriendDto(
    val id: UUID = UUID.randomUUID(),
    val sender: Profile = Profile(),
    val receiver: Profile = Profile(),
    val status: F_status = F_status.PENDING,
    val createdAt: LocalDateTime = LocalDateTime.now(),
): Serializable{

}