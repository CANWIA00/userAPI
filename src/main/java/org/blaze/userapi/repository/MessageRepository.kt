package org.blaze.userapi.repository

import org.blaze.userapi.model.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MessageRepository : JpaRepository<Message, UUID> {
    fun findAllByChatRoomId(chatRoomId: String): List<Message>
}