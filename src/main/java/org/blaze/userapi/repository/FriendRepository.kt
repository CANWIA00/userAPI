package org.blaze.userapi.repository

import org.blaze.userapi.model.F_status
import org.blaze.userapi.model.Friend
import org.blaze.userapi.model.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FriendRepository : JpaRepository<Friend, UUID> {

    fun findBySenderAndReceiver(sender: Profile, receiver: Profile): Friend?
    fun findByReceiverAndStatus(receiver: Profile, status: F_status): List<Friend>?
    fun findBySenderAndStatus(sender: Profile, status: F_status): List<Friend>?

}